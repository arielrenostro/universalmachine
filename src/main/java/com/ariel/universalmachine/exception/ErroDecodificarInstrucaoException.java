package com.ariel.universalmachine.exception;

public abstract class ErroDecodificarInstrucaoException extends UniversalMachineException {

	private static final long serialVersionUID = 1660584326284102531L;

	public ErroDecodificarInstrucaoException(String msg) {
		super(msg);
	}

}
