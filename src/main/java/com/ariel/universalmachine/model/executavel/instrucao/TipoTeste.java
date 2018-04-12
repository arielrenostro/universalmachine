package com.ariel.universalmachine.model.executavel.instrucao;

/**
 *
 * @author Ariel Adonai Souza
 *
 * Define os tipos de teste que a maquina pode executar
 */
public enum TipoTeste {
	ENQUANTO, ATE, SE;

	public static TipoTeste getTipoTesteByName(String name) {
		for (TipoTeste tipo : TipoTeste.values()) {
			if (name.equals(tipo.name())) {
				return tipo;
			}
		}
		return null;
	}
}
