package com.ariel.universalmachine.exception;

public class InstrucaoNullException extends ErroDecodificarInstrucaoException {

	private static final long serialVersionUID = 3815149820762493600L;

	public InstrucaoNullException(int idx) {
		super("Erro ao decodificar Instrucao [null] [" + idx + "]");
	}

}
