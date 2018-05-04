package com.ariel.universalmachine.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.ariel.universalmachine.controller.MaquinaUniversalController;
import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.factory.ControllerFactory;
import com.ariel.universalmachine.util.Util;

@Path("/executadorContexto")
@Provider
public class ExecutadorContextoService extends Service {
	
	@GET
	@Path("/getContextoExecucao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContextoExecucao(@QueryParam("id") String id) {
		try {
			ContextoExecucaoDTO contexto = ControllerFactory.getController(MaquinaUniversalController.class).getExecucaoDTO(id);
			
			Map<String, Object> map = new HashMap<>();
			map.put("contexto", contexto);
			return retornarSucesso(map);
		} catch (Exception e) {
			return retornarErro(e);
		}
	}
	
	@POST
	@Path("/iniciarExecucao")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response iniciarExecucao(String json) {
		try {
			Map<String, Object> mapJson = lerJson(json);
			if (Util.isNotEmpty(mapJson)) {
				String id = (String) mapJson.get("id");
				String codigo = (String) mapJson.get("codigo");
				
				MaquinaUniversalController controller = ControllerFactory.getController(MaquinaUniversalController.class);
				String idExecucao = controller.executar(id, codigo);
			
				Map<String, Object> map = new HashMap<>();
				map.put("id", idExecucao);
				return retornarSucesso(map);
			} else {
				return retornarErro("JSON v�zio");
			}
		} catch (Exception e) {
			return retornarErro(e);
		}
	}
	
}
