package com.ariel.universalmachine.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ariel.universalmachine.controller.Interpretador;
import com.ariel.universalmachine.model.executavel.instrucao.FimBloco;
import com.ariel.universalmachine.model.executavel.instrucao.Instrucao;
import com.ariel.universalmachine.model.executavel.instrucao.OperadorRegistrador;
import com.ariel.universalmachine.model.executavel.instrucao.Senao;
import com.ariel.universalmachine.model.executavel.instrucao.Teste;
import com.ariel.universalmachine.model.executavel.instrucao.TipoOperadorAritmetico;


public class UniversalMachineTest {

	@Test
	public void a001_subtracao() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		interpretador.interpretar();

		List<Instrucao> instrucoes = interpretador.getInstrucoes();
		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(1, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
	}

	@Test
	public void a002_soma() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		interpretador.interpretar();

		List<Instrucao> instrucoes = interpretador.getInstrucoes();
		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(1, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SOMAR, operadorA);
	}

	@Test
	public void a003_variosRegistradoresEOperacoes() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A + 1;");
		codigo.append("B := B + 1;");
		codigo.append("C := C - 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		interpretador.interpretar();

		List<Instrucao> instrucoes = interpretador.getInstrucoes();
		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(3, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		OperadorRegistrador operadorB = (OperadorRegistrador) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SOMAR, operadorA);
		assertOperadorRegistrador("B", TipoOperadorAritmetico.SOMAR, operadorB);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SUBTRAIR, operadorC);
	}

	@Test
	public void a004_variosRegistradoresEComNomesDiferentes() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A:=A+1;");
		codigo.append("B:=       B-1;");
		codigo.append("C:=C-                 1;");
		codigo.append("REGISTRADOR:=REGISTRADOR+1;");
		codigo.append("REGISTRADOR_COM_UNDERLINE:=REGISTRADOR_COM_UNDERLINE+1");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();
		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(5, instrucoes.size());

		OperadorRegistrador operador1 = (OperadorRegistrador) instrucoes.get(0);
		OperadorRegistrador operador2 = (OperadorRegistrador) instrucoes.get(1);
		OperadorRegistrador operador3 = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operador4 = (OperadorRegistrador) instrucoes.get(3);
		OperadorRegistrador operador5 = (OperadorRegistrador) instrucoes.get(4);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SOMAR, operador1);
		assertOperadorRegistrador("B", TipoOperadorAritmetico.SUBTRAIR, operador2);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SUBTRAIR, operador3);
		assertOperadorRegistrador("REGISTRADOR", TipoOperadorAritmetico.SOMAR, operador4);
		assertOperadorRegistrador("REGISTRADOR_COM_UNDERLINE", TipoOperadorAritmetico.SOMAR, operador5);
	}

	@Test
	public void a005_nomeRegistradorInvalido() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A A :=A+1;");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a006_nomeRegistradorInvalido2() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A A :=A A+1;");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a007_nomeRegistradorInvalido3() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A A :=B+1;");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	@Ignore(value = "Pode ser macro!")
	public void a008_nomeRegistradorInvalido4() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A :=+1A");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	@Ignore(value = "Isto é uma macro!")
	public void a009_nomeRegistradorInvalido5() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A :=A+2");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	@Ignore(value = "Pode ser macro!")
	public void a010_nomeRegistradorInvalido6() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A :=+1");

		try {
			new Interpretador(codigo.toString()).interpretar();
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a011_registradorIniciandoComPalavraReservada() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("SEREGISTRADOR := SEREGISTRADOR - 1;");
		codigo.append("ATEREGISTRADOR := ATEREGISTRADOR - 1;");
		codigo.append("ENQUANTOREGISTRADOR := ENQUANTOREGISTRADOR + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(3, instrucoes.size());

		OperadorRegistrador operador1 = (OperadorRegistrador) instrucoes.get(0);
		OperadorRegistrador operador2 = (OperadorRegistrador) instrucoes.get(1);
		OperadorRegistrador operador3 = (OperadorRegistrador) instrucoes.get(2);

		assertOperadorRegistrador("SEREGISTRADOR", TipoOperadorAritmetico.SUBTRAIR, operador1);
		assertOperadorRegistrador("ATEREGISTRADOR", TipoOperadorAritmetico.SUBTRAIR, operador2);
		assertOperadorRegistrador("ENQUANTOREGISTRADOR", TipoOperadorAritmetico.SOMAR, operador3);
	}

	@Test
	public void a012_operacaoSubtracaoETeste() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO");
		codigo.append("  C := C + 1;");
		codigo.append("SENAO");
		codigo.append("  B := B - 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(6, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		Senao senao = (Senao) instrucoes.get(3);
		OperadorRegistrador operadorB = (OperadorRegistrador) instrucoes.get(4);
		FimBloco fimBloco = (FimBloco) instrucoes.get(5);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("B", TipoOperadorAritmetico.SUBTRAIR, operadorB);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBloco, operadorC.getProximaInstrucao());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorB, senao.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorB.getProximaInstrucao());
		Assert.assertNull(fimBloco.getProximaInstrucao());
	}

	@Test
	public void a013_operacaoSubtracaoETesteComSoma() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO");
		codigo.append("  C := C + 1;");
		codigo.append("SENAO");
		codigo.append("  B := B - 1;");
		codigo.append("D := D + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		Senao senao = (Senao) instrucoes.get(3);
		OperadorRegistrador operadorB = (OperadorRegistrador) instrucoes.get(4);
		FimBloco fimBloco = (FimBloco) instrucoes.get(5);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("B", TipoOperadorAritmetico.SUBTRAIR, operadorB);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operadorD);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBloco, operadorC.getProximaInstrucao());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorB, senao.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorB.getProximaInstrucao());
		Assert.assertEquals(operadorD, fimBloco.getProximaInstrucao());
		Assert.assertNull(operadorD.getProximaInstrucao());
	}

	@Test
	public void a014_operacaoSubtracaoEEnquanto() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ENQUANTO A = 0 FACA");
		codigo.append("  C := C + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(4, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		FimBloco fimBloco = (FimBloco) instrucoes.get(3);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(testeA, operadorC.getProximaInstrucao());
		Assert.assertEquals(fimBloco, testeA.getInstrucaoFalso());
		Assert.assertNull(fimBloco.getProximaInstrucao());
	}

	@Test
	public void a015_operacaoSubtracaoEAte() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ATE A = 0 FACA");
		codigo.append("  C := C + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(4, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		FimBloco fimBloco = (FimBloco) instrucoes.get(3);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(fimBloco, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorC, testeA.getInstrucaoFalso());
		Assert.assertEquals(testeA, operadorC.getProximaInstrucao());
		Assert.assertNull(fimBloco.getProximaInstrucao());
	}

	@Test
	public void a016_decodificarBlocos() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D - 1;");
		codigo.append("}");
		codigo.append("SENAO {");
		codigo.append("  E := E + 1;");
		codigo.append("}");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(3);
		Senao senao = (Senao) instrucoes.get(4);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(5);
		FimBloco fimBloco = (FimBloco) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SUBTRAIR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SOMAR, operadorE);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorD.getProximaInstrucao());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorE, senao.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorE.getProximaInstrucao());
		Assert.assertNull(fimBloco.getProximaInstrucao());
	}

	@Test
	public void a017_decodificarBlocos02() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D - 1;");
		codigo.append("} SENAO {");
		codigo.append("  E := E + 1;");
		codigo.append("}");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(3);
		Senao senao = (Senao) instrucoes.get(4);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(5);
		FimBloco fimBloco = (FimBloco) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SUBTRAIR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SOMAR, operadorE);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorD.getProximaInstrucao());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorE, senao.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorE.getProximaInstrucao());
		Assert.assertNull(fimBloco.getProximaInstrucao());
	}

	@Test
	public void a018_decodificarBlocos03() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D - 1;");
		codigo.append("} SENAO {");
		codigo.append("}");
		codigo.append("E := E + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(3);
		Senao senao = (Senao) instrucoes.get(4);
		FimBloco fimBloco = (FimBloco) instrucoes.get(5);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SUBTRAIR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SOMAR, operadorE);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorD.getProximaInstrucao());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(fimBloco, senao.getProximaInstrucao());
		Assert.assertEquals(operadorE, fimBloco.getProximaInstrucao());
		Assert.assertNull(operadorE.getProximaInstrucao());
	}

	@Test
	public void a019_decodificarBlocos04() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 ENTAO {");
		codigo.append("} SENAO {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D - 1;");
		codigo.append("}");
		codigo.append("E := E + 1;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		Senao senao = (Senao) instrucoes.get(2);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(3);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(4);
		FimBloco fimBloco = (FimBloco) instrucoes.get(5);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SUBTRAIR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SOMAR, operadorE);

		assertTesteFimBloco(testeA, fimBloco);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(fimBloco, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(senao, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorC, senao.getProximaInstrucao());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(fimBloco, operadorD.getProximaInstrucao());
		Assert.assertEquals(operadorE, fimBloco.getProximaInstrucao());
		Assert.assertNull(operadorE.getProximaInstrucao());
	}

	@Test
	public void a020_decodificarBlocos05() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ATE A = 0 FACA {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D + 1;");
		codigo.append("}");
		codigo.append("E := E - 1");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(6, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(3);
		FimBloco fimBloco = (FimBloco) instrucoes.get(4);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(5);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SUBTRAIR, operadorE);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(fimBloco, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorC, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(testeA, operadorD.getProximaInstrucao());
		Assert.assertEquals(operadorE, fimBloco.getProximaInstrucao());
		Assert.assertNull(operadorE.getProximaInstrucao());
	}

	@Test
	public void a021_decodificarBlocos06() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ENQUANTO A = 0 FACA {");
		codigo.append("  C := C + 1;");
		codigo.append("  D := D + 1;");
		codigo.append("}");
		codigo.append("E := E - 1");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(6, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste testeA = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(2);
		OperadorRegistrador operadorD = (OperadorRegistrador) instrucoes.get(3);
		FimBloco fimBloco = (FimBloco) instrucoes.get(4);
		OperadorRegistrador operadorE = (OperadorRegistrador) instrucoes.get(5);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operadorC);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operadorD);
		assertOperadorRegistrador("E", TipoOperadorAritmetico.SUBTRAIR, operadorE);

		Assert.assertEquals(testeA, operadorA.getProximaInstrucao());
		Assert.assertEquals(fimBloco, testeA.getInstrucaoFalso());
		Assert.assertEquals(operadorC, testeA.getInstrucaoVerdadeiro());
		Assert.assertEquals(operadorD, operadorC.getProximaInstrucao());
		Assert.assertEquals(testeA, operadorD.getProximaInstrucao());
		Assert.assertEquals(operadorE, fimBloco.getProximaInstrucao());
		Assert.assertNull(operadorE.getProximaInstrucao());
	}

	@Test
	public void a22_comparacaoInvalida01() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ENQUANTO A = 1 FACA");
		codigo.append("  C := C + 1;");

		try {
			Interpretador interpretador = new Interpretador(codigo.toString());
			interpretador.interpretar();

			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a23_comparacaoInvalida02() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ENQUANTO A FACA");
		codigo.append("  C := C + 1;");

		try {
			Interpretador interpretador = new Interpretador(codigo.toString());
			interpretador.interpretar();

			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a24_comparacaoInvalida03() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("ENQUANTO FACA");
		codigo.append("  C := C + 1;");

		try {
			Interpretador interpretador = new Interpretador(codigo.toString());
			interpretador.interpretar();

			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a25_comparacaoInvalida04() {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("SE A = 0 FACA");
		codigo.append("  C := C + 1;");

		try {
			Interpretador interpretador = new Interpretador(codigo.toString());
			interpretador.interpretar();

			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void a26_declaracaoMacro01() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("C := C + 2 {");
		codigo.append("  C := C + 1;");
		codigo.append("  C := C + 1;");
		codigo.append("}");
		codigo.append("C := C + 2;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(3, instrucoes.size());

		OperadorRegistrador operador1 = (OperadorRegistrador) instrucoes.get(0);
		OperadorRegistrador operador2 = (OperadorRegistrador) instrucoes.get(1);
		OperadorRegistrador operador3 = (OperadorRegistrador) instrucoes.get(2);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operador1);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operador2);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SOMAR, operador3);

		Assert.assertEquals(operador2, operador1.getProximaInstrucao());
		Assert.assertEquals(operador3, operador2.getProximaInstrucao());
		Assert.assertNull(operador3.getProximaInstrucao());
	}

	@Test
	public void a27_declaracaoMacro02() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("C := C + 2 {");
		codigo.append("  C := C + 1;");
		codigo.append("  C := C + 1;");
		codigo.append("}");
		codigo.append("D := D + 2;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(3, instrucoes.size());

		OperadorRegistrador operador1 = (OperadorRegistrador) instrucoes.get(0);
		OperadorRegistrador operador2 = (OperadorRegistrador) instrucoes.get(1);
		OperadorRegistrador operador3 = (OperadorRegistrador) instrucoes.get(2);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operador1);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operador2);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operador3);

		Assert.assertEquals(operador2, operador1.getProximaInstrucao());
		Assert.assertEquals(operador3, operador2.getProximaInstrucao());
		Assert.assertNull(operador3.getProximaInstrucao());
	}

	@Test
	public void a28_declaracaoMacro03() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("A := A - 1;");
		codigo.append("C := C + 2 {");
		codigo.append("  C := C + 1;");
		codigo.append("  C := C + 1;");
		codigo.append("}");
		codigo.append("REGISTRADOR := 0 {");
		codigo.append("  ATE REGISTRADOR = 0 FACA {");
		codigo.append("    REGISTRADOR := REGISTRADOR - 1;");
		codigo.append("  }");
		codigo.append("}");
		codigo.append("C := C + A {");
		codigo.append(" ATE A = 0 FACA {");
		codigo.append("   C := C + 1;");
		codigo.append("   A := A - 1;");
		codigo.append("  }");
		codigo.append("}");
		codigo.append("D := 0;");
		codigo.append("D := D + 2;");
		codigo.append("C := 0;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(9, instrucoes.size());

		OperadorRegistrador operadorA = (OperadorRegistrador) instrucoes.get(0);
		Teste ateD = (Teste) instrucoes.get(1);
		OperadorRegistrador operadorD1 = (OperadorRegistrador) instrucoes.get(2);
		FimBloco fimBlocoD = (FimBloco) instrucoes.get(3);
		OperadorRegistrador operadorD2 = (OperadorRegistrador) instrucoes.get(4);
		OperadorRegistrador operadorD3 = (OperadorRegistrador) instrucoes.get(5);
		Teste ateC = (Teste) instrucoes.get(6);
		OperadorRegistrador operadorC = (OperadorRegistrador) instrucoes.get(7);
		FimBloco fimBlocoC = (FimBloco) instrucoes.get(8);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SUBTRAIR, operadorD1);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operadorD2);
		assertOperadorRegistrador("D", TipoOperadorAritmetico.SOMAR, operadorD3);
		assertOperadorRegistrador("C", TipoOperadorAritmetico.SUBTRAIR, operadorC);

		assertTesteFimBloco(ateC, fimBlocoC);
		assertTesteFimBloco(ateD, fimBlocoD);

		Assert.assertEquals(ateD, operadorA.getProximaInstrucao());
		Assert.assertEquals(operadorD1, ateD.getInstrucaoFalso());
		Assert.assertEquals(fimBlocoD, ateD.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBlocoD, ateD.getInstrucaoFimBloco());
		Assert.assertEquals(operadorD2, fimBlocoD.getProximaInstrucao());
		Assert.assertEquals(operadorD3, operadorD2.getProximaInstrucao());
		Assert.assertEquals(ateC, operadorD3.getProximaInstrucao());
		Assert.assertEquals(operadorC, ateC.getInstrucaoFalso());
		Assert.assertEquals(fimBlocoC, ateC.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBlocoC, ateC.getInstrucaoFimBloco());
		Assert.assertEquals(ateC, operadorC.getProximaInstrucao());
		Assert.assertNull(fimBlocoC.getProximaInstrucao());
	}

	@Test
	public void a28_declaracaoMacro04() throws Exception {
		StringBuilder codigo = new StringBuilder();
		codigo.append("REGISTRADOR := 0 {");
		codigo.append("  ATE REGISTRADOR = 0 FACA {");
		codigo.append("    REGISTRADOR := REGISTRADOR - 1;");
		codigo.append("  }");
		codigo.append("}");
		codigo.append("REGISTRADOR_UM := REGISTRADOR_DOIS {");
		codigo.append("  REGISTRADOR_UM := 0;");
		codigo.append("  ATE REGISTRADOR_DOIS = 0 FACA {");
		codigo.append("    REGISTRADOR_UM   := REGISTRADOR_UM   + 1;");
		codigo.append("    REGISTRADOR_DOIS := REGISTRADOR_DOIS - 1;");
		codigo.append("  }");
		codigo.append("}");
		codigo.append("A := B;");

		Interpretador interpretador = new Interpretador(codigo.toString());
		List<Instrucao> instrucoes = interpretador.interpretar();

		Assert.assertNotNull(instrucoes);
		Assert.assertEquals(7, instrucoes.size());

		Teste ateA = (Teste) instrucoes.get(0);
		OperadorRegistrador operadorA1 = (OperadorRegistrador) instrucoes.get(1);
		FimBloco fimBlocoA = (FimBloco) instrucoes.get(2);
		Teste ateB = (Teste) instrucoes.get(3);
		OperadorRegistrador operadorA2 = (OperadorRegistrador) instrucoes.get(4);
		OperadorRegistrador operadorB = (OperadorRegistrador) instrucoes.get(5);
		FimBloco fimBlocoB = (FimBloco) instrucoes.get(6);

		assertOperadorRegistrador("A", TipoOperadorAritmetico.SUBTRAIR, operadorA1);
		assertOperadorRegistrador("A", TipoOperadorAritmetico.SOMAR, operadorA2);
		assertOperadorRegistrador("B", TipoOperadorAritmetico.SUBTRAIR, operadorB);

		assertTesteFimBloco(ateA, fimBlocoA);
		assertTesteFimBloco(ateB, fimBlocoB);

		Assert.assertEquals(operadorA1, ateA.getInstrucaoFalso());
		Assert.assertEquals(fimBlocoA, ateA.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBlocoA, ateA.getInstrucaoFimBloco());
		Assert.assertEquals(ateB, fimBlocoA.getProximaInstrucao());
		Assert.assertEquals(operadorA2, ateB.getInstrucaoFalso());
		Assert.assertEquals(fimBlocoB, ateB.getInstrucaoVerdadeiro());
		Assert.assertEquals(fimBlocoB, ateB.getInstrucaoFimBloco());
		Assert.assertEquals(operadorB, operadorA2.getProximaInstrucao());
		Assert.assertEquals(ateB, operadorB.getProximaInstrucao());
		Assert.assertNull(fimBlocoB.getProximaInstrucao());
	}

	private void assertTesteFimBloco(Teste teste, FimBloco fimBloco) {
		Assert.assertEquals(teste, fimBloco.getInstrucaoInicioBloco());
		Assert.assertEquals(fimBloco, teste.getInstrucaoFimBloco());
	}

	private void assertOperadorRegistrador(String nomeRegistrador, TipoOperadorAritmetico tipoOperador, OperadorRegistrador instrucao) {
		Assert.assertNotNull(instrucao);
		Assert.assertEquals(tipoOperador, instrucao.getTipo());
		Assert.assertEquals(nomeRegistrador, instrucao.getNomeRegistrador());
	}
}


