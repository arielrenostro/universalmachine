package com.ariel.universalmachine.exception;

public class ErroSintaxeException extends UniversalMachineException {

	private static final long serialVersionUID = -1629042421768707702L;

	public ErroSintaxeException(int idxLinha) {
		super("Erro de sintaxe na linha [" + idxLinha + "]");
	}

}
