package com.ariel.universalmachine.model.executavel.instrucao;

/**
 * Define a instrução SENAO
 *
 * @author ariel
 */
public class Senao extends Nada {

	private static final long serialVersionUID = -4221804165933676206L;

	@Override
	public String toString() {
		return "SENAO";
	}

	@Override
	public TipoInstrucao getTipoInstrucao() {
		return TipoInstrucao.SENAO;
	}

	@Override
	public String getCodigo() {
		return "} SENAO {";
	}
}
