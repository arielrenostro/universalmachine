package com.ariel.universalmachine.model;

/**
 *
 * @author ariel
 *
 * Define as operacoes que a maquina exerce sob os registradores
 * Nao alterar a ordem dos enums
 */
public enum TipoOperador {

	ATRIBUICAO(":="),
	COMPARACAO("=");

	private String code;

	TipoOperador(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
