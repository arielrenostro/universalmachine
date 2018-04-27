package com.ariel.universalmachine.controller;

import java.util.Random;

import com.ariel.universalmachine.model.Registrador;

public class RegistradorController extends Controller {
	
	private Random random = new Random();

	public Registrador novo(String nome) {
		Registrador registrador = new Registrador();
		registrador.setNome(nome);
		registrador.setDado(getValorInicial());
		return registrador;
	}

	private int getValorInicial() {
		int valor = random.nextInt(100);
		if (0 > valor) {
			valor *= -1;
		}
		return valor;
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
