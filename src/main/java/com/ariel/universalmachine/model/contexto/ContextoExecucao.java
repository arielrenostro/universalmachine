package com.ariel.universalmachine.model.contexto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ariel.universalmachine.model.Registrador;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;

/**
 * Define o contexto de execução de um código
 *
 * @author Ariel Adonai Souza
 */
public class ContextoExecucao implements Serializable {

	private static final long serialVersionUID = -3197269022766853932L;

	private Map<String, Registrador> registradores = new HashMap<>();
	private List<Instrucao> instrucoes = new ArrayList<>(); // Usar mais tarde para mostrar as isntrucoes que serao executadas
	private List<Instrucao> instrucoesExecucao = new ArrayList<>();
	private Long qntInstrucoesExecutadas = 0L;
	private StatusContextoExecucao status = StatusContextoExecucao.NAO_INICIADO;

	public StatusContextoExecucao getStatus() {
		return status;
	}

	public void setStatus(StatusContextoExecucao status) {
		this.status = status;
	}

	public Map<String, Registrador> getRegistradores() {
		return registradores;
	}

	public void setRegistradores(Map<String, Registrador> registradores) {
		this.registradores = registradores;
	}

	public Long getQntInstrucoesExecutadas() {
		return qntInstrucoesExecutadas;
	}

	public void setQntInstrucoesExecutadas(Long qntInstrucoesExecutadas) {
		this.qntInstrucoesExecutadas = qntInstrucoesExecutadas;
	}

	public List<Instrucao> getInstrucoes() {
		return instrucoes;
	}

	public void setInstrucoes(List<Instrucao> instrucoes) {
		this.instrucoes = instrucoes;
	}

	public List<Instrucao> getInstrucoesExecucao() {
		return instrucoesExecucao;
	}

	public void setInstrucoesExecucao(List<Instrucao> instrucoesExecucao) {
		this.instrucoesExecucao = instrucoesExecucao;
	}

}
