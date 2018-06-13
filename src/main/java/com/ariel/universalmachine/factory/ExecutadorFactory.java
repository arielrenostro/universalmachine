package com.ariel.universalmachine.factory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ariel.universalmachine.controller.ExecutadorController;
import com.ariel.universalmachine.model.CancellableRunnable;
import com.ariel.universalmachine.model.MutableBoolean;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;

public abstract class ExecutadorFactory {

	private static final Logger LOGGER = Logger.getLogger(ExecutadorFactory.class);
	private static final long DURACAO_MAXIMA_CONTEXTO_EXECUCAO = TimeUnit.MILLISECONDS.convert(20, TimeUnit.MINUTES);

	private static final long TIMEOUT_EXECUCAO_THREAD_LIMPEZA_CONTEXTO = 1;
	private static final TimeUnit TIMEOUT_EXECUCAO_THREAD_LIMPEZA_CONTEXTO_UNIDADE = TimeUnit.MINUTES;

	private static final long TIMEOUT_THREAD_EXECUCAO = 5;
	private static final TimeUnit TIMEOUT_THREAD_EXECUCAO_UNIDADE = TimeUnit.MINUTES;

	private static final Map<String, ContextoExecucao> execucoes = new HashMap<>();

	private static ScheduledExecutorService executor;
	private static ScheduledExecutorService executorLimpezaContexto;

	static {
		executorLimpezaContexto = Executors.newSingleThreadScheduledExecutor();
		executorLimpezaContexto.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				ContextoExecucao valor = null;
				Date ultimaExecucao = null;
				long tempoAtual = new Date().getTime();

				for (Entry<String, ContextoExecucao> entry : execucoes.entrySet()) {
					valor = entry.getValue();
					ultimaExecucao = valor.getUltimaExecucao();

					if (ultimaExecucao != null && DURACAO_MAXIMA_CONTEXTO_EXECUCAO <= tempoAtual - ultimaExecucao.getTime()) {
						execucoes.remove(entry.getKey());
					}
				}
			}
		}, TIMEOUT_EXECUCAO_THREAD_LIMPEZA_CONTEXTO, TIMEOUT_EXECUCAO_THREAD_LIMPEZA_CONTEXTO, TIMEOUT_EXECUCAO_THREAD_LIMPEZA_CONTEXTO_UNIDADE);
	}

	private synchronized static ScheduledExecutorService getExecutor() {
		if (null == executor) {
			executor = Executors.newScheduledThreadPool(5);
		}
		return executor;
	}

	public synchronized static ContextoExecucao getExecucao(String chave) {
		ContextoExecucao contexto = execucoes.get(chave);
		if (null != contexto) {
			contexto.setUltimaExecucao(new Date());
		}
		return contexto;
	}

	public synchronized static String iniciarNovoContexto(ContextoExecucao contexto) {
		CancellableRunnable runnable = instanciarRunnable(contexto);
		Runnable canceladorRunnable = instanciarCanceladorRunnable(runnable);

		getExecutor().submit(runnable);
		getExecutor().schedule(canceladorRunnable, TIMEOUT_THREAD_EXECUCAO, TIMEOUT_THREAD_EXECUCAO_UNIDADE);

		String identificador = UUID.randomUUID().toString();
		execucoes.put(identificador, contexto);

		return identificador;
	}

	private static Runnable instanciarCanceladorRunnable(CancellableRunnable runnable) {
		return new Runnable() {
			@Override
			public void run() {
				runnable.cancel();
			}
		};
	}

	private static CancellableRunnable instanciarRunnable(ContextoExecucao contexto) {
		return new CancellableRunnable() {

			private MutableBoolean cancelar = new MutableBoolean(false);

			@Override
			public void run() {
				try {
					ControllerFactory.getController(ExecutadorController.class).executarPorContexto(contexto, cancelar);
				} finally {
					if (cancelar.getValue()) {
						contexto.setStatus(StatusContextoExecucao.TEMPO_EXCEDIDO);
						LOGGER.warn("Thread timeout");
					} else {
						contexto.setStatus(StatusContextoExecucao.FINALIZADO);
					}
				}
			}

			@Override
			public void cancel() {
				cancelar.setValue(true);
			}
		};
	}
}
