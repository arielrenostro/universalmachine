package com.ariel.universalmachine.exception;

import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

public class InstrucaoFimBlocoNaoEncontradaException extends ErroDecodificarInstrucaoException {

	private static final long serialVersionUID = 6567444665814483372L;

	public InstrucaoFimBlocoNaoEncontradaException(Instrucao instrucao, Instrucao instrucaoFimBloco) {
		super("Não encontrada instrução de fim de bloco [" + instrucaoFimBloco + "] para instrução [" + instrucao + "]");
	}
}
