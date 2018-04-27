package com.ariel.universalmachine.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.ariel.universalmachine.controller.MaquinaUniversalController;
import com.ariel.universalmachine.dto.ContextoExecucaoDTO;
import com.ariel.universalmachine.factory.ControllerFactory;

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
	public Response iniciarExecucao(@PathParam("codigo") String codigo, @PathParam("id") String id) {
		try {
			MaquinaUniversalController controller = ControllerFactory.getController(MaquinaUniversalController.class);
			String idExecucao = controller.executar(id, codigo);
			
			Map<String, Object> map = new HashMap<>();
			map.put("id", idExecucao);
			return retornarSucesso(map);
		} catch (Exception e) {
			return retornarErro(e);
		}
	}
	
}
