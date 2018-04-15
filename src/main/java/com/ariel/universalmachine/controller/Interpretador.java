package com.ariel.universalmachine.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ariel.universalmachine.exception.ErroDecodificarInstrucaoException;
import com.ariel.universalmachine.exception.ErroInternoProcessamentoMacroException;
import com.ariel.universalmachine.exception.ErroMacroNaoDeclarada;
import com.ariel.universalmachine.exception.ErroSintaxeException;
import com.ariel.universalmachine.exception.FalhouInterpretadorException;
import com.ariel.universalmachine.exception.InstrucaoFimBlocoNaoEncontradaException;
import com.ariel.universalmachine.exception.InstrucaoNullException;
import com.ariel.universalmachine.model.TipoOperador;
import com.ariel.universalmachine.model.executavel.instrucao.FimBloco;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;
import com.ariel.universalmachine.model.executavel.instrucao.OperadorRegistrador;
import com.ariel.universalmachine.model.executavel.instrucao.Senao;
import com.ariel.universalmachine.model.executavel.instrucao.Teste;
import com.ariel.universalmachine.model.executavel.instrucao.TipoOperadorAritmetico;
import com.ariel.universalmachine.model.executavel.instrucao.TipoTeste;
import com.ariel.universalmachine.model.macro.Assinatura;
import com.ariel.universalmachine.model.macro.MacroAssinatura;
import com.ariel.universalmachine.util.Util;

public class Interpretador {

	private static TipoTeste[] tiposTeste = TipoTeste.values();
	private static Pattern pNomeRegistrador = Pattern.compile("^([A-Z])(([A-Z_0-9]+)|)$"); // Inicia com A-Z e depois pode conter somente A-Z _ 0-9 ou nada
	private static Pattern pNomeRegistradorUmChar = Pattern.compile("[A-Z_0-9]+");
	private static Pattern pQuebraLinha = Pattern.compile("\n");

	private String codigo;
	private int idx = 0;
	private int idxLinha = 1;
	private List<MacroAssinatura> macrosAssinatura = new ArrayList<>();

	private boolean falhou = false;

	private List<Instrucao> instrucoes = new ArrayList<>();

	public Interpretador(String codigo) {
		super();
		this.codigo = codigo;
	}

	public Interpretador(String codigo, List<MacroAssinatura> macrosAssinatura, int idxLinha) {
		this(codigo);
		this.macrosAssinatura = macrosAssinatura;
		this.idxLinha = idxLinha - getQuantidadeQuebraLinhas(codigo);
	}

	private int getQuantidadeQuebraLinhas(String codigo) {
		Matcher matcher = pQuebraLinha.matcher(codigo);
		int qntQuebraLinha = 1;
		while (matcher.find()) {
			qntQuebraLinha++;
		}
		return qntQuebraLinha;
	}

	public List<Instrucao> interpretar() throws Exception {
		try {
			processarInstrucao();
			popularProximasInstrucoes();
		} catch (Exception e) {
			falhou = true;
			throw e;
		}
		return getInstrucoes();
	}

	private void popularProximasInstrucoes() throws ErroDecodificarInstrucaoException {
		Instrucao instrucaoFimBloco = null; // N alocar mais espaço em memoria
		int idxInstrucaoFimBloco = 0;

		Instrucao instrucao = null;
		int idxInstrucao = 0;
		while (idxInstrucao < instrucoes.size()) {
			instrucao = instrucoes.get(idxInstrucao++);

			if (instrucao == null) {
				throw new InstrucaoNullException(idxInstrucao);
			}

			if (OperadorRegistrador.class.equals(instrucao.getClass()) || FimBloco.class.equals(instrucao.getClass())) {

				if (null == instrucao.getProximaInstrucao()) {
					definirProximaInstrucaoParaOperadorRegistrador(instrucao, idxInstrucao);
				}

			} else if (Teste.class.equals(instrucao.getClass())) {

				instrucaoFimBloco = ((Teste) instrucao).getInstrucaoFimBloco();
				idxInstrucaoFimBloco = instrucoes.indexOf(instrucaoFimBloco);

				if (0 > idxInstrucaoFimBloco) {
					throw new InstrucaoFimBlocoNaoEncontradaException(instrucao, instrucaoFimBloco);
				}

				idxInstrucao = idxInstrucaoFimBloco - 1;
			}
		}
	}

	private void definirProximaInstrucaoParaOperadorRegistrador(Instrucao instrucao, int idxInstrucao) {
		if (idxInstrucao < instrucoes.size()) {
			instrucao.setProximaInstrucao(instrucoes.get(idxInstrucao));
		}
	}

	private void processarInstrucao() throws Exception {
		while (temMaisCaracteres()) {
			String instrucao = getProximaInstrucao();

			if (instrucao.isEmpty()) {
				continue;
			}

			TipoOperador tipoOperador = getTipoOperadorInstrucao(instrucao);

			if (null == tipoOperador) {
				throw new ErroSintaxeException(instrucao, idxLinha);
			}

			switch (tipoOperador) {
				case ATRIBUICAO:
					validarAtribuicao(instrucao);
					break;
				case COMPARACAO:
					processarComparacao(instrucao);
					break;
				default:
					throw new ErroSintaxeException(instrucao, idxLinha);
			}
		}
	}

	private void processarComparacao(String instrucao) throws Exception {
		TipoTeste tipoTeste = getTipoTesteLinha(instrucao);
		if (null == tipoTeste) {
			throw new ErroSintaxeException(instrucao, idxLinha);
		}
		String registrador = getPrimeiroNomeRegistrador(instrucao.substring(tipoTeste.name().length() + 1));

		validarComparacao(instrucao, registrador, tipoTeste);

		Teste teste = new Teste();
		teste.setTipo(tipoTeste);
		teste.setNomeRegistrador(registrador);

		List<Instrucao> instrucoesComparacao = processarBlocoInstrucao();
		instrucoes.add(teste);
		instrucoes.addAll(instrucoesComparacao);

		FimBloco fimBloco = new FimBloco();
		teste.setInstrucaoFimBloco(fimBloco);
		fimBloco.setInstrucaoInicioBloco(teste);

		if (0 < instrucoesComparacao.size()) {
			if (TipoTeste.ATE.equals(tipoTeste) || TipoTeste.ENQUANTO.equals(tipoTeste)) {
				if (TipoTeste.ATE.equals(tipoTeste)) {
					teste.setInstrucaoVerdadeiro(fimBloco);
					teste.setInstrucaoFalso(Util.getPrimeiroElemento(instrucoesComparacao));
				} else {
					teste.setInstrucaoVerdadeiro(Util.getPrimeiroElemento(instrucoesComparacao));
					teste.setInstrucaoFalso(fimBloco);
				}
				Util.getUltimoElemento(instrucoesComparacao).setProximaInstrucao(teste);
			} else {
				teste.setInstrucaoVerdadeiro(Util.getPrimeiroElemento(instrucoesComparacao));
				Util.getUltimoElemento(instrucoesComparacao).setProximaInstrucao(fimBloco);
			}
		} else {
			teste.setInstrucaoVerdadeiro(fimBloco);
			teste.setInstrucaoFalso(fimBloco);
		}


		if (TipoTeste.SE.equals(tipoTeste)) {
			String proximaInstrucao = getProximaInstrucao();
			if (proximaInstrucao.contains("SENAO")) {
				Senao senao = new Senao();
				teste.setInstrucaoFalso(senao);

				List<Instrucao> instrucoesSenao = processarBlocoInstrucao();

				if (0 < instrucoesSenao.size()) {
					senao.setProximaInstrucao(Util.getPrimeiroElemento(instrucoesSenao));
					Util.getUltimoElemento(instrucoesSenao).setProximaInstrucao(fimBloco);
				} else {
					senao.setProximaInstrucao(fimBloco);
				}

				instrucoes.add(senao);
				instrucoes.addAll(instrucoesSenao);
			} else {
				throw new ErroSintaxeException(instrucao, idxLinha);
			}
		}

		instrucoes.add(fimBloco);
	}

	private void validarComparacao(String instrucao, String registrador, TipoTeste tipoTeste) throws ErroSintaxeException {
		instrucao = instrucao.substring(registrador.length() + tipoTeste.name().length() + 1);

		boolean esperandoComparacao = true;
		boolean esperandoZero = false;

		String str = "";
		char charLeitura = ' ';
		int idxInstrucao = 0;
		while (idxInstrucao < instrucao.length()) {
			charLeitura = instrucao.charAt(idxInstrucao++);

			if (esperandoComparacao) {
				if ('=' == charLeitura) {
					esperandoZero = true;
					esperandoComparacao = false;
				} else if (' ' != charLeitura) {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}
			} else if (esperandoZero) {
				if ('0' == charLeitura) {
					esperandoZero = false;
				} else if (' ' != charLeitura) {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}
			} else {
				str += charLeitura;
				if ("FACA".equals(str)) {
					break;
				}
			}
		}

		if (idxInstrucao == instrucao.length() - 1) {
			throw new ErroSintaxeException(instrucao, idxLinha);
		}
	}

	private String getPrimeiroNomeRegistrador(String instrucao) throws ErroSintaxeException {
		String str = "";
		String ultimoCharLeitura = "";

		int idxInstrucao = 0;
		while (idxInstrucao < instrucao.length()) {
			ultimoCharLeitura = "" + instrucao.charAt(idxInstrucao++);
			str += ultimoCharLeitura;

			if (somenteCaracteresBrancos(str) || isCharValidoParaRegistrador(ultimoCharLeitura)) {
				continue;
			}

			str = str.substring(0, str.length() - 1).trim(); // Pega toda a string menos o ultimo caractere

			if (isNomeRegistradorValido(str)) {
				return str;
			}
		}
		throw new ErroSintaxeException(instrucao, idxLinha);
	}

	private List<Instrucao> processarBlocoInstrucao() throws ErroSintaxeException, Exception {
		List<Instrucao> instrucoesComparacao = null;
		String proximaInstrucao = getProximaInstrucao();
		if (proximaInstrucao.endsWith("{")) {
			String blocoInstrucoes = getBlocoInstrucoes(proximaInstrucao);
			Interpretador interpretador = new Interpretador(blocoInstrucoes, macrosAssinatura, idxLinha);
			instrucoesComparacao = interpretador.interpretar();
		} else {
			Interpretador interpretador = new Interpretador(proximaInstrucao, macrosAssinatura, idxLinha);
			instrucoesComparacao = interpretador.interpretar();
		}

		return instrucoesComparacao;
	}

	private String getBlocoInstrucoes(String instrucao) throws ErroSintaxeException {
		String str = instrucao;
		int qntAbreChaves = 1;
		int qntFechaChaves = 0;

		while (temMaisCaracteres() && qntAbreChaves != qntFechaChaves) {
			str += getProximaInstrucao();
			qntAbreChaves = Util.countMatches(str, "[{]");
			qntFechaChaves = Util.countMatches(str, "[}]");
		}

		str = str.trim();

		if (qntAbreChaves != qntFechaChaves) {
			throw new ErroSintaxeException(instrucao, idxLinha);
		}

		return str.substring(1, str.length() - 1);
	}

	private void validarAtribuicao(String instrucao) throws Exception {
		try {
			processarAtribuicao(instrucao);
		} catch (ErroSintaxeException e) {
			processarAtribuicaoMacro(instrucao);
		}
	}

	private void processarAtribuicaoMacro(String instrucao) throws Exception {
		Assinatura assinatura = getAssinaturaMacro(instrucao);
		MacroAssinatura macroAssinatura = getMacroAssinaturaPorAssinatura(assinatura);

		if (null == macroAssinatura) {
			if (instrucao.endsWith("{")) {
				declararMacroNova(assinatura);
			} else {
				throw new ErroMacroNaoDeclarada(idxLinha);
			}
		} else {
			processarMacro(macroAssinatura, assinatura);
		}
	}

	private void processarMacro(MacroAssinatura macroAssinatura, Assinatura assinatura) throws ErroInternoProcessamentoMacroException {
		List<Instrucao> instrucoesConvertidas = new ArrayList<>();

		Teste instrucaoTesteAtual = null;
		OperadorRegistrador instrucaoOperadorAtual = null;
		Senao instrucaoSenaoAtual = null;
		FimBloco instrucaoFimBlocoAtual = null;

		List<Instrucao> instrucoesMacro = macroAssinatura.getInstrucoes();
		for (Instrucao instrucaoMacro : instrucoesMacro) {

			if (Teste.class.equals(instrucaoMacro.getClass())) {

				instrucaoTesteAtual = new Teste();
				instrucaoTesteAtual.setNomeRegistrador(instrucaoMacro.getNomeRegistrador());
				instrucaoTesteAtual.setTipo(((Teste) instrucaoMacro).getTipo());
				instrucoesConvertidas.add(instrucaoTesteAtual);

			} else if (OperadorRegistrador.class.equals(instrucaoMacro.getClass())) {

				instrucaoOperadorAtual = new OperadorRegistrador();
				instrucaoOperadorAtual.setNomeRegistrador(instrucaoMacro.getNomeRegistrador());
				instrucaoOperadorAtual.setTipo(((OperadorRegistrador) instrucaoMacro).getTipo());
				instrucoesConvertidas.add(instrucaoOperadorAtual);

			} else if (Senao.class.equals(instrucaoMacro.getClass())) {

				instrucaoSenaoAtual = new Senao();
				instrucoesConvertidas.add(instrucaoSenaoAtual);

			} else if (FimBloco.class.equals(instrucaoMacro.getClass())) {

				instrucaoFimBlocoAtual = new FimBloco();
				instrucoesConvertidas.add(instrucaoFimBlocoAtual);

			} else {
				throw new ErroInternoProcessamentoMacroException(macroAssinatura);
			}
		}

		Set<Entry<String, String>> entrySetMacroAssinatura = macroAssinatura.getAssinatura().getParametros().entrySet();
		int indexAux = -1;
		Instrucao instrucaoConvertida = null;
		Instrucao instrucaoAuxiliar = null;
		Instrucao instrucaoMacro = null;
		Teste instrucaoTesteMacro = null;
		FimBloco instrucaoFimBlocoMacro = null;
		String registrador = null;
		for (int idxInstrucoes = 0; idxInstrucoes < instrucoesConvertidas.size(); idxInstrucoes++) {

			instrucaoConvertida = instrucoesConvertidas.get(idxInstrucoes);
			instrucaoMacro = instrucoesMacro.get(idxInstrucoes);
			registrador = getRegistradorAssinaturaMacro(entrySetMacroAssinatura, assinatura, instrucaoMacro);

			instrucaoConvertida.setNomeRegistrador(registrador);

			if (Teste.class.equals(instrucaoConvertida.getClass())) {

				instrucaoTesteAtual = (Teste) instrucaoConvertida;
				instrucaoTesteMacro = (Teste) instrucaoMacro;

				indexAux = instrucoesMacro.indexOf(instrucaoTesteMacro.getInstrucaoFalso());
				instrucaoAuxiliar = -1 < indexAux ? instrucoesConvertidas.get(indexAux) : null;
				instrucaoTesteAtual.setInstrucaoFalso(instrucaoAuxiliar);

				indexAux = instrucoesMacro.indexOf(instrucaoTesteMacro.getInstrucaoFimBloco());
				instrucaoAuxiliar = -1 < indexAux ? instrucoesConvertidas.get(indexAux) : null;
				instrucaoTesteAtual.setInstrucaoFimBloco((FimBloco) instrucaoAuxiliar);

				indexAux = instrucoesMacro.indexOf(instrucaoTesteMacro.getInstrucaoVerdadeiro());
				instrucaoAuxiliar = -1 < indexAux ? instrucoesConvertidas.get(indexAux) : null;
				instrucaoTesteAtual.setInstrucaoVerdadeiro(instrucaoAuxiliar);

			} else if (OperadorRegistrador.class.equals(instrucaoConvertida.getClass()) //
					|| Senao.class.equals(instrucaoConvertida.getClass()) //
					|| FimBloco.class.equals(instrucaoConvertida.getClass())) {

				indexAux = instrucoesMacro.indexOf(instrucaoMacro.getProximaInstrucao());
				instrucaoAuxiliar = -1 < indexAux ? instrucoesConvertidas.get(indexAux) : null;
				instrucaoConvertida.setProximaInstrucao(instrucaoAuxiliar);

				if (FimBloco.class.equals(instrucaoConvertida.getClass())) {
					instrucaoFimBlocoAtual = (FimBloco) instrucaoConvertida;
					instrucaoFimBlocoMacro = (FimBloco) instrucaoMacro;

					indexAux = instrucoesMacro.indexOf(instrucaoFimBlocoMacro.getInstrucaoInicioBloco());
					instrucaoAuxiliar = -1 < indexAux ? instrucoesConvertidas.get(indexAux) : null;
					instrucaoFimBlocoAtual.setInstrucaoInicioBloco(instrucaoAuxiliar);
				}
			}
		}

		instrucoes.addAll(instrucoesConvertidas);
	}

	private String getRegistradorAssinaturaMacro(Set<Entry<String, String>> entrySetMacroAssinatura, Assinatura assinatura, Instrucao instrucaoMacro) {
		Entry<String, String> entryRegistradorAnterior = entrySetMacroAssinatura//
				.parallelStream()//
				.filter(entry -> entry.getValue().equals(instrucaoMacro.getNomeRegistrador()))//
				.findFirst()//
				.orElse(null);
		if (null != entryRegistradorAnterior) {
			return assinatura.getParametros().get(entryRegistradorAnterior.getKey());
		}
		return instrucaoMacro.getNomeRegistrador();
	}

	private void declararMacroNova(Assinatura assinatura) throws ErroSintaxeException, Exception {
		String blocoInstrucoes = getBlocoInstrucoes("{");
		Interpretador interpretador = new Interpretador(blocoInstrucoes, macrosAssinatura, idxLinha);
		List<Instrucao> instrucoesMacro = interpretador.interpretar();

		MacroAssinatura macroAssinatura = new MacroAssinatura();
		macroAssinatura.setAssinatura(assinatura);
		macroAssinatura.setInstrucoes(instrucoesMacro);

		macrosAssinatura.add(macroAssinatura);
	}

	private MacroAssinatura getMacroAssinaturaPorAssinatura(Assinatura assinatura) {
		for (MacroAssinatura macroAssinatura : macrosAssinatura) {
			if (assinatura.getNomeMacro().equals(macroAssinatura.getAssinatura().getNomeMacro())) {
				return macroAssinatura;
			}
		}
		return null;
	}

	private void processarAtribuicao(String instrucao) throws ErroSintaxeException {
		String str = "";
		String nomeRegistrador = null;
		String ultimoCharLeitura = null;
		TipoOperadorAritmetico tipoOperadorAritmetico = null;

		boolean esperandoNomeRegistrador = true;
		boolean esperandoOperador = false;
		boolean esperandoSegundoNomeRegistrador = false;
		boolean esperandoOperadorArtimetico = false;
		boolean esperandoUm = false;
		boolean fim = false;

		int idxInstrucao = 0;
		while (idxInstrucao < instrucao.length()) {
			ultimoCharLeitura = "" + instrucao.charAt(idxInstrucao++);
			str += ultimoCharLeitura;

			if (esperandoNomeRegistrador || esperandoSegundoNomeRegistrador) {

				if (somenteCaracteresBrancos(str) || " ".equals(ultimoCharLeitura) || isCharValidoParaRegistrador(ultimoCharLeitura)) {
					continue;
				}

				idxInstrucao--; // Volta o caractere da proxima comparação. Ex: A:=A+1. o idxInstrucao já aponta para :, então volta 1.
				str = str.substring(0, str.length() - 1).trim(); // Pega toda a string menos o ultimo caractere

				if (isNomeRegistradorValido(str)) {
					if (nomeRegistrador == null) {
						nomeRegistrador = str;
					} else if (!nomeRegistrador.equals(str)) {
						throw new ErroSintaxeException(instrucao, idxLinha);
					}
				} else {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}

				if (esperandoNomeRegistrador) {
					esperandoNomeRegistrador = false;
					esperandoOperador = true;
				} else {
					esperandoSegundoNomeRegistrador = false;
					esperandoOperadorArtimetico = true;
				}

			} else if (esperandoOperador) {

				if (somenteCaracteresBrancos(str) || !str.contains(TipoOperador.ATRIBUICAO.getCode())) {
					continue;
				}

				str = str.trim();

				if (!TipoOperador.ATRIBUICAO.getCode().equals(str)) {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}

				esperandoOperador = false;
				esperandoSegundoNomeRegistrador = true;

			} else if (esperandoOperadorArtimetico) {

				if (somenteCaracteresBrancos(str) || !(str.contains(TipoOperadorAritmetico.SOMAR.getCode()) || str.contains(TipoOperadorAritmetico.SUBTRAIR.getCode()))) {
					continue;
				}

				str = str.trim();

				if (TipoOperadorAritmetico.SOMAR.getCode().equals(str)) {
					tipoOperadorAritmetico = TipoOperadorAritmetico.SOMAR;
				} else if (TipoOperadorAritmetico.SUBTRAIR.getCode().equals(str)) {
					tipoOperadorAritmetico = TipoOperadorAritmetico.SUBTRAIR;
				} else {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}

				esperandoOperadorArtimetico = false;
				esperandoUm = true;

			} else if (esperandoUm) {

				if (somenteCaracteresBrancos(str) || !str.contains("1")) {
					continue;
				}

				str = str.trim();

				if (!"1".equals(str)) {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}

				esperandoUm = false;
				fim = true;

			} else if (fim) {

				if (somenteCaracteresBrancos(str)) {
					continue;
				}

				str = str.trim();

				if (!str.endsWith(";") && !str.isEmpty()) {
					throw new ErroSintaxeException(instrucao, idxLinha);
				}
			}

			str = "";
		}

		if (!fim || null == nomeRegistrador || null == tipoOperadorAritmetico) {
			throw  new ErroSintaxeException(instrucao, idxLinha);
		}

		OperadorRegistrador operador = new OperadorRegistrador();
		operador.setNomeRegistrador(nomeRegistrador);
		operador.setTipo(tipoOperadorAritmetico);

		instrucoes.add(operador);
	}

	/**
	 * Valida se todos os caracteres da string são brancos
	 * @param ultimoCaractereStr
	 * @return
	 */
	private boolean somenteCaracteresBrancos(String ultimoCaractereStr) {
		if (ultimoCaractereStr.isEmpty()) {
			return false;
		}

		int idxStr = 0;
		while (idxStr < ultimoCaractereStr.length()) {
			if (ultimoCaractereStr.charAt(idxStr++) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Valida se existe somente 1 nome de registrador na string e se ele é valido
	 * @param nomeRegistrador
	 * @return se o nome do registrador é valido
	 */
	private boolean isNomeRegistradorValido(String nomeRegistrador) {
		if (null != nomeRegistrador && !nomeRegistrador.isEmpty()) {
			Matcher matcher = pNomeRegistrador.matcher(nomeRegistrador);
			int countValidos = 0;
			while (matcher.find()) {
				countValidos++;
				if (1 < countValidos || !nomeRegistrador.equals(matcher.group())) {
					return false;
				}
			}

			if (1 == countValidos) {
				return true;
			}
		}
		return false;
	}

	private boolean isCharValidoParaRegistrador(String charRegistrador) {
		if (null != charRegistrador && !charRegistrador.isEmpty()) {
			Matcher matcher = pNomeRegistradorUmChar.matcher(charRegistrador);
			int countValidos = 0;
			while (matcher.find()) {
				countValidos++;
				if (1 < countValidos || !charRegistrador.equals(matcher.group())) {
					return false;
				}
			}

			if (1 == countValidos) {
				return true;
			}
		}
		return false;
	}

	private Assinatura getAssinaturaMacro(String instrucao) throws ErroSintaxeException {
		Assinatura assinatura = new Assinatura();
		Map<String, String> parametros = assinatura.getParametros();

		String nomeRegistrador = getPrimeiroNomeRegistrador(instrucao);
		assinatura.setRecebedor(nomeRegistrador);

		int idxAtribuicao = instrucao.indexOf(TipoOperador.ATRIBUICAO.getCode());
		if (!nomeRegistrador.equals(instrucao.substring(0, idxAtribuicao).trim())) {
			throw new ErroSintaxeException(instrucao, idxLinha);
		}

		instrucao = instrucao.substring(idxAtribuicao + TipoOperador.ATRIBUICAO.getCode().length()).trim();

		if (instrucao.endsWith(";") || instrucao.endsWith("{")) {
			instrucao = instrucao.substring(0, instrucao.length() - 1).trim();
		}

		nomeRegistrador = "";
		String ultimoCharLeitura = "";
		int idxInstrucao = 0;
		while (idxInstrucao < instrucao.length()) {
			ultimoCharLeitura = "" + instrucao.charAt(idxInstrucao++);

			if (isCharValidoParaRegistrador(ultimoCharLeitura)) {
				nomeRegistrador += ultimoCharLeitura;
				if (idxInstrucao < instrucao.length()) {
					continue;
				}
			}

			if (!nomeRegistrador.isEmpty()) {
				if (!isNomeRegistradorValido(nomeRegistrador)) { //|| (!instrucao.equals(nomeRegistrador) && ' ' != instrucao.charAt(instrucao.length() - (nomeRegistrador.length() + 1)))) {
					nomeRegistrador = "";
					continue;
				}
				parametros.put("REGISTRADOR_" + parametros.size(), nomeRegistrador);
				nomeRegistrador = "";
			}
		}

		if (!parametros.values().contains(assinatura.getRecebedor())) {
			parametros.put("REGISTRADOR_" + parametros.size(), assinatura.getRecebedor());
		}

		String nomeMacro = instrucao;
		for (Entry<String, String> entry : parametros.entrySet()) {
			if (nomeMacro.length() == entry.getValue().length()) {
				nomeMacro = nomeMacro.replaceAll(entry.getValue(), entry.getKey());
			} else {
				nomeMacro = nomeMacro.replaceAll(entry.getValue() + " ", entry.getKey() + " ");
				nomeMacro = nomeMacro.replaceAll(" " + entry.getValue(), " " +entry.getKey());
			}
		}

		assinatura.setNomeMacro(nomeMacro);

		return assinatura;
	}

	private TipoOperador getTipoOperadorInstrucao(String instrucao) {
		TipoTeste tipoTeste = getTipoTesteLinha(instrucao);

		if (null != tipoTeste) {
			return TipoOperador.COMPARACAO;
		} else if (instrucao.contains(TipoOperador.ATRIBUICAO.getCode())) {
			return TipoOperador.ATRIBUICAO;
		}

		return null;
	}

	private TipoTeste getTipoTesteLinha(String instrucao) {
		for (TipoTeste tipo : tiposTeste) {
			if (instrucao.startsWith(tipo.name().concat(" ")) ) {
				return tipo;
			}
		}
		return null;
	}

	/**
	 * Retorna a próxima instrução da string ou inicio de bloco ou fim de bloco
	 * @return próxima instrução
	 * @throws ErroSintaxeException
	 */
	private String getProximaInstrucao() throws ErroSintaxeException {
		String instrucao = "";
		while (temMaisCaracteres()) {
			instrucao += getProximoCaractere();
			if (instrucao.endsWith(";") //
					|| instrucao.endsWith("{") //
					|| instrucao.endsWith("}") //
					|| instrucao.endsWith("SENAO") //
					|| instrucao.endsWith("ENTAO") //
					|| instrucao.endsWith("FACA")) {
				avancarAteProximaLetra();
				break;
			}
		}
		return instrucao.trim();
	}

	private void avancarAteProximaLetra() {
		while (temMaisCaracteres()) {
			if (' ' != getProximoCaractere()) {
				voltarCaractere();
				break;
			}
		}
	}

	public List<Instrucao> getInstrucoes() throws FalhouInterpretadorException {
		if (falhou) {
			throw new FalhouInterpretadorException();
		}
		return Collections.unmodifiableList(instrucoes);
	}

	private void voltarCaractere() {
		idx--;
		if (codigo.charAt(idx) == '\n') {
			idxLinha--;
		}
	}

	private char getProximoCaractere() {
		char charAt = codigo.charAt(idx++);
		if (charAt == '\n') {
			idxLinha++;
		}
		return charAt;
	}

	private boolean temMaisCaracteres() {
		return idx < codigo.length();
	}

}
