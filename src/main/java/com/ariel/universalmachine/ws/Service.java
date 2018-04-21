package com.ariel.universalmachine.ws;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Service {

	protected Response retornarJSON(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String json = mapper.writeValueAsString(obj);
			return Response.status(200).entity(json).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}
}
