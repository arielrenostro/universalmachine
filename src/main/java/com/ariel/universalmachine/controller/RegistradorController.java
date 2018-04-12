package com.ariel.universalmachine.controller;

import com.ariel.universalmachine.model.Registrador;

public class RegistradorController {

	public Registrador novo(String nome) {
		Registrador registrador = new Registrador();
		registrador.setNome(nome);
		registrador.setDado(getValorInicial());
		return registrador;
	}

	private int getValorInicial() {
		int random = Double.valueOf(Math.random()).intValue();
		if (0 > random) {
			random *= -1;
		}
		return random;
	}

	public void somar(Registrador registrador) {
		registrador.setDado(registrador.getDado() + 1);
	}

	public void subtrair(Registrador registrador) {
		if (0 == registrador.getDado()) {
			return;
		}
		registrador.setDado(registrador.getDado() - 1);
	}

}
