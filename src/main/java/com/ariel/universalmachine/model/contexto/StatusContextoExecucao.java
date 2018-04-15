package com.ariel.universalmachine.model.contexto;

/**
 * Define os status do contexto de execução
 *
 * @author ariel
 *
 */
public enum StatusContextoExecucao  {

	NAO_INICIADO("Não iniciado"), //
	RODANDO("Rodando"), //
	FINALIZADO("Finalizado");
	
	private String code;
	
	StatusContextoExecucao(final String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

}
