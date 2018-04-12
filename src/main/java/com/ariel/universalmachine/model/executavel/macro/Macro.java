package com.ariel.universalmachine.model.executavel.macro;

import java.util.ArrayList;
import java.util.List;

import com.ariel.universalmachine.model.executavel.Executavel;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

/**
 *
 * @author Ariel Adonai Souza
 *
 * Utilizada para execucao do codigo.
 * Nessa macro devem estar somente as instrucoes basicas da maquina universal, nada de macros.
 */
public class Macro implements Executavel {

	private static final long serialVersionUID = 4170009168579120826L;

	private List<Instrucao> instrucoes = new ArrayList<>();

	public List<Instrucao> getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(List<Instrucao> instrucoes) {
		this.instrucoes = instrucoes;
	}

}
