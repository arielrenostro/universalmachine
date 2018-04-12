package com.ariel.universalmachine.model.macro;

import java.util.ArrayList;
import java.util.List;

import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

/**
 *
 * @author Ariel Adonai Souza
 *
 * Utilizada somente na geracao do codigo "bruto".
 * Nessa macro devem estar todos os objetos Executavel necessarios.
 * No Controller essa classe retornara uma instancia de Macro.
 */
public class MacroAssinatura {

	private Assinatura assinatura;
	private List<Instrucao> instrucoes = new ArrayList<>();

	public Assinatura getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(Assinatura assinatura) {
		this.assinatura = assinatura;
	}

	public List<Instrucao> getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(List<Instrucao> instrucoes) {
		this.instrucoes = instrucoes;
	}

}
