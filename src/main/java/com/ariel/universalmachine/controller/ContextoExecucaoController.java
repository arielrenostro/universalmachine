package com.ariel.universalmachine.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.exception.ContextoExecucaoException;
import com.ariel.universalmachine.factory.ExecutadorFactory;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

public class ContextoExecucaoController extends Controller {

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

		return ExecutadorFactory.iniciarNovoContexto(contexto);
	}

	private List<Instrucao> interpretarInstrucoes(String codigo) throws Exception {
		Interpretador interpretador = new Interpretador(codigo);
		return interpretador.interpretar();
	}

	private boolean podeExecutar(ContextoExecucaoDTO contexto) {
		return null == contexto || !(StatusContextoExecucao.RODANDO.equals(contexto.getStatus()) || StatusContextoExecucao.NAO_INICIADO.equals(contexto.getStatus()));
	}
	
	public ContextoExecucaoDTO getExecucaoDTO(String chave) {
		if (null != chave) {
			ContextoExecucao execucao = ExecutadorFactory.getExecucao(chave);
			return instanciarContextoExecucaoDTO(execucao);
		}
		return null;
	}

	private ContextoExecucaoDTO instanciarContextoExecucaoDTO(ContextoExecucao execucao) {
		ContextoExecucaoDTO contextoExecucaoDTO = new ContextoExecucaoDTO();
		
		if (null != execucao) {
			contextoExecucaoDTO.setQntInstrucoesExecutadas(execucao.getQntInstrucoesExecutadas());
			contextoExecucaoDTO.setRegistradores(new HashMap<>(execucao.getRegistradores()));
			contextoExecucaoDTO.setStatus(execucao.getStatus());
			contextoExecucaoDTO.setInstrucoes(new ArrayList<>(execucao.getInstrucoes()));
		}
		
		return contextoExecucaoDTO;
	}
}
