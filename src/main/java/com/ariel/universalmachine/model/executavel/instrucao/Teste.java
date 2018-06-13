package com.ariel.universalmachine.model.executavel.instrucao;

/**
 * @author Ariel Adonai Souza
 */
public class Teste extends Instrucao {

	private static final long serialVersionUID = -2302047024795267117L;

	private TipoTeste tipo;
	private Instrucao instrucaoVerdadeiro;
	private Instrucao instrucaoFalso;
	private FimBloco instrucaoFimBloco;

	public TipoTeste getTipo() {
		return tipo;
	}

	public void setTipo(TipoTeste tipo) {
		this.tipo = tipo;
	}

	public Instrucao getInstrucaoVerdadeiro() {
		return instrucaoVerdadeiro;
	}

	public void setInstrucaoVerdadeiro(Instrucao instrucaoVerdadeiro) {
		this.instrucaoVerdadeiro = instrucaoVerdadeiro;
	}

	public Instrucao getInstrucaoFalso() {
		return instrucaoFalso;
	}

	public void setInstrucaoFalso(Instrucao instrucaoFalso) {
		this.instrucaoFalso = instrucaoFalso;
	}

	public FimBloco getInstrucaoFimBloco() {
		return instrucaoFimBloco;
	}

	public void setInstrucaoFimBloco(FimBloco instrucaoFimBloco) {
		this.instrucaoFimBloco = instrucaoFimBloco;
	}

	@Override
	public String toString() {
		return getNomeRegistrador() + " [" + tipo + "]";
	}

	@Override
	public TipoInstrucao getTipoInstrucao() {
		return TipoInstrucao.TESTE;
	}

	@Override
	public String getCodigo() {
		return getTipo().name() + " " + getNomeRegistrador() + " = 0 FACA {";
	}

}
