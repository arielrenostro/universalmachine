package com.ariel.universalmachine.exception;

public class ErroSintaxeException extends UniversalMachineException {

	private static final long serialVersionUID = -1629042421768707702L;

	public ErroSintaxeException(String instrucao, int idxLinha) {
		super(getString(instrucao, idxLinha, null));
	}

	protected static String getString(String instrucao, int idxLinha, String complemento) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Erro de sintaxe próximo a linha [");
		stringBuilder.append(idxLinha);
		stringBuilder.append("]");
		if (null != instrucao) {
			stringBuilder.append(" instrução [");
			stringBuilder.append(instrucao);
			stringBuilder.append("]");
		}
		if (null != complemento) {
			stringBuilder.append(" ");
			stringBuilder.append(complemento);
		}
		return stringBuilder.toString();
	}

	protected ErroSintaxeException(String msg) {
		super(msg);
	}

}
