package com.ariel.universalmachine.exception;

public class ErroAtribuicaoFimEsperadoException extends ErroSintaxeException {

	private static final long serialVersionUID = -344806475055326151L;

	public ErroAtribuicaoFimEsperadoException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, "Esperado um fim de instrução!"));
	}
}
