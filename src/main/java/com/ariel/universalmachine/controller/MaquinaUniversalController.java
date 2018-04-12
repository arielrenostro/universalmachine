package com.ariel.universalmachine.controller;

import java.io.Serializable;

import com.ariel.universalmachine.model.contexto.ContextoExecucao;

public class MaquinaUniversalController implements Serializable {

	private static final long serialVersionUID = 4352906176413408632L;

	private ContextoExecucaoController contextoExecucaoController = new ContextoExecucaoController();

	public String executar(ContextoExecucao contexto, String codigo) throws Exception {
		return contextoExecucaoController.executar(contexto, codigo);
	}

	public ContextoExecucao atualizarEstado(String idExecucao) {
		if (null != idExecucao) {
			return Executador.getExecucao(idExecucao);
		}
		return null;
	}

}
