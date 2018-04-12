package com.ariel.universalmachine.exception;

import com.ariel.universalmachine.model.macro.MacroAssinatura;

public class ErroInternoProcessamentoMacroException extends Exception {

	private static final long serialVersionUID = -2744512681731983776L;

	public ErroInternoProcessamentoMacroException(MacroAssinatura macroAssinatura) {
		super("Erro ao processar a macro [" + macroAssinatura.getAssinatura().getNomeMacro() + "]");
	}

}
