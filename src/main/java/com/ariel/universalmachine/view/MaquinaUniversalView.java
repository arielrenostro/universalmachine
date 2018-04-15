package com.ariel.universalmachine.view;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.ariel.universalmachine.controller.MaquinaUniversalController;
import com.ariel.universalmachine.model.contexto.ContextoExecucao;

@Named
@SessionScoped
public class MaquinaUniversalView implements Serializable {

	private static final long serialVersionUID = -8496448641512314362L;

	private MaquinaUniversalController controller = new MaquinaUniversalController();
	private ContextoExecucao contexto;
	private String codigo;
	private String idExecucao;

	public void atualizarEstado() {
		contexto = controller.atualizarEstado(idExecucao);
	}

	public void executar() {
		try {
			idExecucao = controller.executar(contexto, codigo);
			addInfoMessage("Execução enviada! Clique em \"Resultado\" para visualizar a execução");
		} catch (Exception e) {
			addErrorMessage(e);
		}
	}

	private void addInfoMessage(String string) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", string));
	}

	private void addErrorMessage(Exception e) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", e.getMessage()));
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public ContextoExecucao getContexto() {
		return contexto;
	}

	public void setContexto(ContextoExecucao contexto) {
		this.contexto = contexto;
	}

}
