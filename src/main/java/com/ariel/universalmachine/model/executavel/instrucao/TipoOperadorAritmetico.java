package com.ariel.universalmachine.model.executavel.instrucao;

/**
 *
 * @author Ariel Adonai Souza
 *
 * Define as operacoes que a maquina consegue executar
 */
public enum TipoOperadorAritmetico {

	SOMAR("+"),
	SUBTRAIR("-");

	private String code;

	private TipoOperadorAritmetico(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
