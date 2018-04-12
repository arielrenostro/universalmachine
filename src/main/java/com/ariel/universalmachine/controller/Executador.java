package com.ariel.universalmachine.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ariel.universalmachine.model.Registrador;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;
import com.ariel.universalmachine.model.executavel.instrucao.OperadorRegistrador;
import com.ariel.universalmachine.model.executavel.instrucao.Teste;
import com.ariel.universalmachine.util.Util;

public class Executador {

	private static Map<String, ContextoExecucao> execucoes = new HashMap<>();
	private static ExecutorService executor = Executors.newFixedThreadPool(5);
	private static RegistradorController registradorController = new RegistradorController();

	public synchronized static ContextoExecucao getExecucao(String chave) {
		return execucoes.get(chave);
	}

	public synchronized static Map<String, ContextoExecucao> getExecucoes() {
		return execucoes;
	}

	public static String executar(ContextoExecucao contexto) {
		String identificador = UUID.randomUUID().toString();
		executor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					executarPorContexto(contexto);
				} finally {
					contexto.setStatus(StatusContextoExecucao.FINALIZADO);
				}
			}
		});
		execucoes.put(identificador, contexto);
		return identificador;
	}

	private static void executarPorContexto(ContextoExecucao contexto) {
		contexto.setStatus(StatusContextoExecucao.RODANDO);
		executarInstrucoesContexto(contexto);
	}

	private static void executarInstrucoesContexto(ContextoExecucao contexto) {
		Instrucao proximaInstrucao = Util.getPrimeiroElemento(contexto.getInstrucoesExecucao());
		Map<String, Registrador> registradores = contexto.getRegistradores();

		String nomeRegistrador = null;
		Registrador registrador = null;
		Instrucao instrucao = proximaInstrucao;

		while (null != instrucao) {

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
}
