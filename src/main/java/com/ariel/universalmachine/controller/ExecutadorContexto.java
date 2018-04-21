package com.ariel.universalmachine.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.model.MutableBoolean;
import com.ariel.universalmachine.model.Registrador;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;
import com.ariel.universalmachine.model.executavel.instrucao.OperadorRegistrador;
import com.ariel.universalmachine.model.executavel.instrucao.Teste;
import com.ariel.universalmachine.util.Util;

/**
 * {@link ExecutadorContexto} define a classe de execucao dos codigos da maquina de norman. Recebe um contexto de execucao e inicia o loop de execucao até atingir o final ou timeout da thread.
 * 
 * @author ariel
 *
 */
public class ExecutadorContexto {

	private static final Logger LOGGER = Logger.getLogger(ExecutadorContexto.class);
	private static final Map<String, ContextoExecucao> execucoes = new HashMap<>(); // TODO implementar um timeout para remover o contexto da memoria. pode estourar a memoria.
	private static final RegistradorController registradorController = new RegistradorController();
	private static ScheduledExecutorService executor;
	
	private synchronized static ScheduledExecutorService getExecutor() {
		if (null == executor) {
			executor = Executors.newScheduledThreadPool(5);
		}
		return executor;
	}
	
	public synchronized static ContextoExecucao getExecucao(String chave) {
		return execucoes.get(chave);
	}

	public synchronized static Map<String, ContextoExecucao> getExecucoes() {
		return execucoes;
	}

	public synchronized static String executar(ContextoExecucao contexto) {
		CancellableRunnable runnable = instanciarRunnable(contexto);
		Runnable canceladorRunnable = instanciarCanceladorRunnable(runnable);
		 
		getExecutor().submit(runnable);
		getExecutor().schedule(canceladorRunnable, 5, TimeUnit.MINUTES);
		
		String identificador = UUID.randomUUID().toString();
		execucoes.put(identificador, contexto);
		
		return identificador;
	}

	private static Runnable instanciarCanceladorRunnable(CancellableRunnable runnable) {
		return new Runnable(){
			@Override
			public void run(){
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
					executarPorContexto(contexto, cancelar);
				} finally {
					if (cancelar.isValue()) {
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

	private static void executarPorContexto(ContextoExecucao contexto, MutableBoolean cancelar) {
		contexto.setStatus(StatusContextoExecucao.RODANDO);
		executarInstrucoesContexto(contexto, cancelar);
	}

	private static void executarInstrucoesContexto(ContextoExecucao contexto, MutableBoolean cancelar) {
		Instrucao proximaInstrucao = Util.getPrimeiroElemento(contexto.getInstrucoesExecucao());
		Map<String, Registrador> registradores = contexto.getRegistradores();

		String nomeRegistrador = null;
		Registrador registrador = null;
		Instrucao instrucao = proximaInstrucao;

		while (null != instrucao && !cancelar.isValue()) {

			nomeRegistrador = instrucao.getNomeRegistrador();
			registrador = getRegistrador(nomeRegistrador, registradores);

			contexto.setQntInstrucoesExecutadas(contexto.getQntInstrucoesExecutadas() + 1L);

			if (Teste.class.equals(instrucao.getClass())) {

				if (0 == registrador.getDado()) {
					instrucao = ((Teste) instrucao).getInstrucaoVerdadeiro();
				} else {
					instrucao = ((Teste) instrucao).getInstrucaoFalso();
				}

				continue;

			} else if(OperadorRegistrador.class.equals(instrucao.getClass())) {

				switch (((OperadorRegistrador) instrucao).getTipo()) {
					case SOMAR:
						registradorController.somar(registrador);
						break;

					case SUBTRAIR:
						registradorController.subtrair(registrador);
						break;
					default:
						break;
				}
			}

			instrucao = instrucao.getProximaInstrucao();
		}
	}

	private static Registrador getRegistrador(String nomeRegistrador, Map<String, Registrador> registradores) {
		if (null != nomeRegistrador && !nomeRegistrador.isEmpty()) {
			Registrador registrador =  registradores.get(nomeRegistrador);
			if (null == registrador) {
				registrador = registradorController.novo(nomeRegistrador);
				registradores.put(nomeRegistrador, registrador);
			}
			return registrador;
		}
		return null;
	}

	public synchronized static ContextoExecucaoDTO getExecucaoDTO(String chave) {
		ContextoExecucao execucao = getExecucao(chave);
		return instanciarContextoExecucaoDTO(execucao);
	}

	private static ContextoExecucaoDTO instanciarContextoExecucaoDTO(ContextoExecucao execucao) {
		ContextoExecucaoDTO contextoExecucaoDTO = new ContextoExecucaoDTO();
		
		if (null != execucao) {
			contextoExecucaoDTO.setQntInstrucoesExecutadas(execucao.getQntInstrucoesExecutadas());
			contextoExecucaoDTO.setRegistradores(new HashMap<>(execucao.getRegistradores()));
			contextoExecucaoDTO.setStatus(execucao.getStatus());
		}
		
		return contextoExecucaoDTO;
	}
}
