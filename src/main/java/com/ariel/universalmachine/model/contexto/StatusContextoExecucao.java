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
	FINALIZADO("Finalizado"), //
	TEMPO_EXCEDIDO("Tempo excedido");
	
	private String code;
	
	StatusContextoExecucao(final String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

}
