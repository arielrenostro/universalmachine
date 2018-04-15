package com.ariel.universalmachine.exception;

public class ErroAtribuicaoOperadorAritmeticoEsperadoException extends	ErroSintaxeException {

	private static final long serialVersionUID = 4278131708155124138L;

	public ErroAtribuicaoOperadorAritmeticoEsperadoException(String instrucao,	int idxLinha) {
		super(getString(instrucao, idxLinha, "Esperado um operador aritmético!"));
	}
}
