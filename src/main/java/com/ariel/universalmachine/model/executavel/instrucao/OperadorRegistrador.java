package com.ariel.universalmachine.model.executavel.instrucao;

/**
 *
 * @author Ariel Adonai Souza
 */
public class OperadorRegistrador extends Instrucao {

	private static final long serialVersionUID = 7315158528263667637L;

	private TipoOperadorAritmetico tipo;

	public TipoOperadorAritmetico getTipo() {
		return tipo;
	}

	public void setTipo(TipoOperadorAritmetico tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return getNomeRegistrador() + " [" + tipo + "]";
	}

}
