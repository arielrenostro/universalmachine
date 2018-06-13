package com.ariel.universalmachine.model.executavel.instrucao;

/**
 * Define uma instrucao para apontar o final de um bloco de instrucoes.
 *
 * @author Ariel Adonai Souza
 */
public class FimBloco extends Nada {

	private static final long serialVersionUID = 3498180188776583607L;

	private Instrucao instrucaoInicioBloco;

	public Instrucao getInstrucaoInicioBloco() {
		return instrucaoInicioBloco;
	}

	public void setInstrucaoInicioBloco(Instrucao instrucaoInicioBloco) {
		this.instrucaoInicioBloco = instrucaoInicioBloco;
	}

	@Override
	public String toString() {
		return "FIMBLOCO";
	}

	@Override
	public TipoInstrucao getTipoInstrucao() {
		return TipoInstrucao.FIM_BLOCO;
	}

	@Override
	public String getCodigo() {
		return "}";
	}
}
