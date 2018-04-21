package com.ariel.universalmachine.controller;

import java.io.Serializable;

import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.factory.ControllerFactory;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;

public class MaquinaUniversalController extends Controller implements Serializable {

	private static final long serialVersionUID = 4352906176413408632L;

	public String executar(ContextoExecucaoDTO contexto, String codigo) throws Exception {
		return ControllerFactory.getController(ContextoExecucaoController.class).executar(contexto, codigo);
	}

	public ContextoExecucaoDTO getExecucaoDTO(String idExecucao) {
		if (null != idExecucao) {
			return ExecutadorContexto.getExecucaoDTO(idExecucao);
		}
		return null;
	}

	public ContextoExecucao getExecucao(String idExecucao) {
		if (null != idExecucao) {
			return ExecutadorContexto.getExecucao(idExecucao);
		}
		return null;
	}

}
