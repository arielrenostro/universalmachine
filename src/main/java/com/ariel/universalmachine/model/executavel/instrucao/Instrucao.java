package com.ariel.universalmachine.model.executavel.instrucao;

import com.ariel.universalmachine.model.executavel.Executavel;

/**
 * @author Ariel Adonai Souza
 */
public abstract class Instrucao implements Executavel {

	private static final long serialVersionUID = -5714621637037878285L;

	private String nomeRegistrador;
	private Instrucao proximaInstrucao;

	public String getNomeRegistrador() {
		return nomeRegistrador;
	}

	public void setNomeRegistrador(String nomeRegistrador) {
		this.nomeRegistrador = nomeRegistrador;
	}

	public Instrucao getProximaInstrucao() {
		return proximaInstrucao;
	}

	public void setProximaInstrucao(Instrucao proximaInstrucao) {
		this.proximaInstrucao = proximaInstrucao;
	}

	public abstract TipoInstrucao getTipoInstrucao();

	public abstract String getCodigo();
}
