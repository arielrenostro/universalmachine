package com.ariel.universalmachine.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.exception.ContextoExecucaoException;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

public class ContextoExecucaoController extends Controller implements Serializable {

	private static final long serialVersionUID = -6104907050513368963L;

	public String executar(ContextoExecucaoDTO contexto, String codigo) throws Exception {
		if (podeExecutar(contexto)) {
			return iniciarNovaExecucao(codigo);
		}
		throw new ContextoExecucaoException("Status do contexto não permite iniciar a execução!");
	}

	private String iniciarNovaExecucao(String codigo) throws Exception {
		List<Instrucao> instrucoes  = interpretarInstrucoes(codigo);

		ContextoExecucao contexto = new ContextoExecucao();
		contexto.setInstrucoes(Collections.unmodifiableList(instrucoes));
		contexto.setInstrucoesExecucao(instrucoes);

		return ExecutadorContexto.executar(contexto);
	}

	private List<Instrucao> interpretarInstrucoes(String codigo) throws Exception {
		Interpretador interpretador = new Interpretador(codigo);
		return interpretador.interpretar();
	}

	private boolean podeExecutar(ContextoExecucaoDTO contexto) {
		return null == contexto || !(StatusContextoExecucao.RODANDO.equals(contexto.getStatus()) || StatusContextoExecucao.NAO_INICIADO.equals(contexto.getStatus()));
	}

}
