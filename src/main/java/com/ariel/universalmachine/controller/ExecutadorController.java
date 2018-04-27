package com.ariel.universalmachine.controller;

import java.util.Map;

import com.ariel.universalmachine.factory.ControllerFactory;
import com.ariel.universalmachine.model.MutableBoolean;
import com.ariel.universalmachine.model.Registrador;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;
import com.ariel.universalmachine.model.executavel.instrucao.OperadorRegistrador;
import com.ariel.universalmachine.model.executavel.instrucao.Teste;
import com.ariel.universalmachine.util.Util;

/**
 * {@link ExecutadorController} define a classe de execucao dos codigos da maquina de norman. Recebe um contexto de execucao e inicia o loop de execucao até atingir o final ou timeout da thread.
 * 
 * @author ariel
 *
 */
public class ExecutadorController extends Controller {
	
	private RegistradorController registradorController;

	public ExecutadorController() {
		registradorController = ControllerFactory.getController(RegistradorController.class);
	}
	
	public void executarPorContexto(ContextoExecucao contexto, MutableBoolean cancelar) {
		contexto.setStatus(StatusContextoExecucao.RODANDO);
		executarInstrucoesContexto(contexto, cancelar);
	}

	private void executarInstrucoesContexto(ContextoExecucao contexto, MutableBoolean cancelar) {
		Instrucao proximaInstrucao = Util.getPrimeiroElemento(contexto.getInstrucoesExecucao());
		Map<String, Registrador> registradores = contexto.getRegistradores();

		String nomeRegistrador = null;
		Registrador registrador = null;
		Class<?> instrucaoClass = null;
		Instrucao instrucao = proximaInstrucao;

		while (null != instrucao && !cancelar.getValue()) {

			instrucaoClass = instrucao.getClass();
			nomeRegistrador = instrucao.getNomeRegistrador();
			registrador = getRegistrador(nomeRegistrador, registradores);

			contexto.setQntInstrucoesExecutadas(contexto.getQntInstrucoesExecutadas() + 1L);

			if (Teste.class.equals(instrucaoClass)) {

				if (0 == registrador.getDado()) {
					instrucao = ((Teste) instrucao).getInstrucaoVerdadeiro();
				} else {
					instrucao = ((Teste) instrucao).getInstrucaoFalso();
				}

				continue;

			} else if(OperadorRegistrador.class.equals(instrucaoClass)) {

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

	private Registrador getRegistrador(String nomeRegistrador, Map<String, Registrador> registradores) {
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
