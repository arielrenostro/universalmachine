package com.ariel.universalmachine.ws;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.ariel.universalmachine.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Service {

	private static final Logger LOGGER = Logger.getLogger(Service.class);
	private ObjectMapper mapper;
	
	protected ObjectMapper getObjectMapper() {
		if (null == mapper) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	protected Response retornarSucesso(Map<String, Object> map) {
		if (Util.isNotEmpty(map)) {
			map.put("status", "SUCESSO");
			
			String json = getJson(map);
			return getResponse(200, json);
		}
		return retornarErro("Nenhum elemento informado para retorno");
	}

	protected Response retornarErro(String msg) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "ERRO");
		map.put("erro", msg);
		
		String json = getJson(map);
		return getResponse(500, json);
	}

	protected Response retornarErro(Exception e) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "ERRO");
		map.put("erro", e.getMessage());
		map.put("stackTrace", Arrays.stream(e.getStackTrace()).map(stack -> stack.toString()).collect(Collectors.joining("\n")));
		
		String json = getJson(map);
		return getResponse(500, json);
	}

	private Response getResponse(int httpCode, String json) {
		return Response.status(httpCode).entity(json).build();
	}

	private String getJson(Map<String, Object> map) {
		try {
			return getObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> lerJson(String json) {
		try {
			return getObjectMapper().readValue(json, HashMap.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
}
