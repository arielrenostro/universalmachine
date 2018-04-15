package com.ariel.universalmachine.exception;

public class ErroAtribuicaoNumeroUmEsperadoException extends ErroSintaxeException {

	private static final long serialVersionUID = -4160164970023458759L;

	public ErroAtribuicaoNumeroUmEsperadoException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, "Esperado número 1!"));
	}
}
