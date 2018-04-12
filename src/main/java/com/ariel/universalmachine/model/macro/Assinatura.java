package com.ariel.universalmachine.model.macro;

import java.util.HashMap;
import java.util.Map;

/**
 * Define a assinatura das macros
 *
 * @author Ariel Adonai Souza
 */
public class Assinatura {

	private String recebedor;
	private String nomeMacro;
	private Map<String, String> parametros = new HashMap<>();

	public String getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}

	public String getNomeMacro() {
		return nomeMacro;
	}

	public void setNomeMacro(String nomeMacro) {
		this.nomeMacro = nomeMacro;
	}

	public Map<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, String> parametros) {
		this.parametros = parametros;
	}

}
