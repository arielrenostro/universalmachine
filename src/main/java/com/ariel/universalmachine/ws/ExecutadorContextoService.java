package com.ariel.universalmachine.ws;

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
		ContextoExecucaoDTO contexto = ControllerFactory.getController(MaquinaUniversalController.class).getExecucaoDTO(id);
		
		return retornarJSON(contexto);
	}
	
	@POST
	@Path("/iniciarExecucao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response iniciarExecucao(@PathParam("codigo") String codigo, @PathParam("id") String id) throws Exception {
		MaquinaUniversalController controller = ControllerFactory.getController(MaquinaUniversalController.class);
		
		ContextoExecucaoDTO contextoExecucao = controller.getExecucaoDTO(id);
		String idExecucao = controller.executar(contextoExecucao, id);
		
		return retornarJSON(idExecucao);
	}
	
}
