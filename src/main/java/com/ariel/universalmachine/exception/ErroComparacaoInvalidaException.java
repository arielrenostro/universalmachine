package com.ariel.universalmachine.exception;

public class ErroComparacaoInvalidaException extends ErroSintaxeException {

	private static final long serialVersionUID = -188791310863031397L;

	public ErroComparacaoInvalidaException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, "Comparação não identificada!"));
	}

}
