package com.ariel.universalmachine.exception;

public class ErroMacroNaoDeclarada extends ErroSintaxeException {

	private static final long serialVersionUID = -1443590711103581883L;

	public ErroMacroNaoDeclarada(int idxLinha) {
		super("Macro não declarada próximo a linha [" + idxLinha + "]");
	}

}
