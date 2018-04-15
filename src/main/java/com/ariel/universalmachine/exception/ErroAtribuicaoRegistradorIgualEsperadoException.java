package com.ariel.universalmachine.exception;

public class ErroAtribuicaoRegistradorIgualEsperadoException extends ErroSintaxeException {

	private static final long serialVersionUID = 6157465087499476955L;

	public ErroAtribuicaoRegistradorIgualEsperadoException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, "Esperado o mesmo nome para registrador!"));
	}

}
