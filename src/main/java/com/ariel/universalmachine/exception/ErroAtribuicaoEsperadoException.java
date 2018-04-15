package com.ariel.universalmachine.exception;

public class ErroAtribuicaoEsperadoException extends ErroSintaxeException {

	private static final long serialVersionUID = 6073781684304694280L;

	public ErroAtribuicaoEsperadoException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, "Esperado operador de atribuição!"));
	}
}
