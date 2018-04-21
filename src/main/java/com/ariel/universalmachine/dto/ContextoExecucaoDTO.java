package com.ariel.universalmachine.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ariel.universalmachine.model.Registrador;
import com.ariel.universalmachine.model.contexto.StatusContextoExecucao;


/**
 * {@link ContextoExecucaoDTO} tem por finalidade definir uma classe de transferência de dados do contexto de execução
 * 
 * @author ariel
 *
 */
public class ContextoExecucaoDTO implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	private Map<String, Registrador> registradores = new HashMap<>();
	//private List<Instrucao> instrucoes = new ArrayList<>(); // Usar mais tarde para mostrar as isntrucoes que serao executadas
	private Long qntInstrucoesExecutadas = 0L;
	private StatusContextoExecucao status = StatusContextoExecucao.NAO_INICIADO;
	
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
	
	public StatusContextoExecucao getStatus() {
		return status;
	}
	
	public void setStatus(StatusContextoExecucao status) {
		this.status = status;
	} 
	
}
