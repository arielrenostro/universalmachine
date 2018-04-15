package com.ariel.universalmachine.model;

import java.io.Serializable;

/**
 *
 * @author Ariel Adonai Souza
 */
public class Registrador implements Serializable {

	private static final long serialVersionUID = 9191148962215513550L;

	private String nome;
	private long dado;

	public long getDado() {
		return dado;
	}

	public void setDado(long dado) {
		this.dado = dado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		Registrador outro = (Registrador) obj;
		return getNome().equals(outro.getNome());
	}

}
