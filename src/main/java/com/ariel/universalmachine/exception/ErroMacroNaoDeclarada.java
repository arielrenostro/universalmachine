package com.ariel.universalmachine.exception;

public class ErroMacroNaoDeclarada extends ErroSintaxeException {

	private static final long serialVersionUID = -1443590711103581883L;

	public ErroMacroNaoDeclarada(int idxLinha, String instrucao) {
		super("Macro n�o declarada pr�ximo a linha [" + idxLinha + "] instru��o [" + instrucao + "]");
	}

}
