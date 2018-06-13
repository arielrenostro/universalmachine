package com.ariel.universalmachine.controller;

import java.io.Serializable;

import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.factory.ControllerFactory;
import com.ariel.universalmachine.util.Util;

public class MaquinaUniversalController extends Controller implements Serializable {

	private static final long serialVersionUID = 4352906176413408632L;

	public String executar(String id, String codigo) throws Exception {
		ContextoExecucaoController controller = ControllerFactory.getController(ContextoExecucaoController.class);
		ContextoExecucaoDTO contextoExecucaoDTO = controller.getExecucaoDTO(id);
		return controller.executar(contextoExecucaoDTO, codigo);
	}

	public ContextoExecucaoDTO getExecucaoDTO(String idExecucao) {
		if (Util.isNotEmpty(idExecucao)) {
			return ControllerFactory.getController(ContextoExecucaoController.class).getExecucaoDTO(idExecucao);
		}
		return null;
	}

}
