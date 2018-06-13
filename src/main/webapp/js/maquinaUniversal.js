MaquinaUniversal = {
	// Constantes
	_TEMPO_MENSAGEM_ERRO: 10000,
	_TEMPO_MENSAGEM_INFO: 5000,
	
	// Atributos
	id: "",
	contexto: null,
	mostrarStacktrace: false,
	podeExecutarPool: false,
	
	// Metodos
	executar: function () {
		var json = {
			codigo: $('#codigo').val(),
			id: MaquinaUniversal.id
		};
		$.ajax({
			url: './api/executadorContexto/iniciarExecucao',
			dataType: 'json',
			type: 'post',
			data: JSON.stringify(json),
			contentType: 'application/json',
			success: MaquinaUniversal.iniciarExecucaoSucessoCallback,
			error: MaquinaUniversal.iniciarExecucaoErroCallback
		});
	},
	
	iniciarExecucaoSucessoCallback: function(data, textStatus, jQxhr) {
		var resposta = jQxhr.responseJSON;
		if (null != resposta.id) {
			MaquinaUniversal.id = resposta.id;
			MaquinaUniversal.podeExecutarPool = true;
			
			MaquinaUniversal.atualizarContexto();
			
			MaquinaUniversal.mostrarInfo("Execução enviada! Acompanhe em \"Resultado\"");
			setTimeout(MaquinaUniversal.ocultarMensagem, MaquinaUniversal._TEMPO_MENSAGEM_INFO);
		}
	},
	
	iniciarExecucaoErroCallback: function(jqXhr, textStatus, errorThrown) {
		var resposta = jqXhr.responseJSON;
		
		if (MaquinaUniversal.mostrarStacktrace) {
			console.log(resposta.stackTrace);
		}
		
		MaquinaUniversal.mostrarErro(resposta.erro);
		setTimeout(MaquinaUniversal.ocultarMensagem, MaquinaUniversal._TEMPO_MENSAGEM_ERRO);
	},
	
	mostrarErro: function(erro) {
		var divErro = $('#errorDiv'); // TODO ANIMAR
		divErro.css({'display': 'inline', 'color': '#da6363'});
		
		var errorText = $('#errorText');
		errorText.html(erro);
	},
	
	ocultarMensagem: function () {
		var divErro = $('#errorDiv'); // TODO ANIMAR
		divErro.css('display', 'none');
	},
	
	mostrarInfo: function(info) {
		var divErro = $('#errorDiv'); // TODO ANIMAR
		divErro.css({'display': 'inline', 'color': 'rgb(81, 163, 224)'});
		
		var errorText = $('#errorText');
		errorText.html(info);
	},
	
	atualizarContexto: function() {
		var id = MaquinaUniversal.id;
		
		if (null != id && "" != id) {
			$.ajax({
				url: './api/executadorContexto/getContextoExecucao?id=' + id,
				type: 'get',
				success: MaquinaUniversal.getContextoExecucaoSucessoCallback,
				error: MaquinaUniversal.getContextoExecucaoErroCallback
			});
		}
	},
	
	getContextoExecucaoSucessoCallback: function(data, textStatus, jQxhr) {
		var resposta = jQxhr.responseJSON;
		if (null == resposta) {
			MaquinaUniversal.podeExecutarPool = false;
			
			MaquinaUniversal.mostrarErro("Sem resposta do pool!");
			setTimeout(MaquinaUniversal.ocultarMensagem, MaquinaUniversal._TEMPO_MENSAGEM_ERRO);
			return;
		}
		
		if (null != resposta.contexto) {
			MaquinaUniversal.contexto = resposta.contexto;
			
			MaquinaUniversal.atualizarCamposHtmlPorContexto();
			MaquinaUniversal.definirEstadoPool();
		}
	},
	
	getContextoExecucaoErroCallback: function(jqXhr, textStatus, errorThrown) {
		var resposta = jqXhr.responseJSON;
		
		console.log("######### - PARANDO POOL - #########\nERRO: " + resposta.erro + "\nStacktrace: \n" + resposta.stackTrace);
		MaquinaUniversal.podeExecutarPool = false;
	},
	
	atualizarCamposHtmlPorContexto: function() {
		var contexto = MaquinaUniversal.contexto;
		
		if (null != contexto) {
			$("#qntInstrucoes").html(contexto.instrucoes.length);
			$("#qntInstrucoesExecutadas").html(contexto.qntInstrucoesExecutadas);
			
			var status = "Status não identificado ";
			if ("FINALIZADO" == contexto.status) {
				status = "Finalizado";
			} else if ("NAO_INICIADO" == contexto.status) {
				status = "Não iniciado";
			} else if ("RODANDO" == contexto.status) {
				status = "Rodando";
			} else if ("TEMPO_EXCEDIDO" == contexto.status) {
				status = "Tempo excedido";
			} else {
				status += contexto.status;
			}
			$("#statusExecucao").html(status);
			
			MaquinaUniversal.limparTabelasContexto();
			
			var tabela = $("#tabelaRegistradores").find("tbody");
			for (var nomeRegistrador in contexto.registradores) {
				var registrador = contexto.registradores[nomeRegistrador];
				
				tabela.append("<tr><td>" + registrador.nome + "</td><td style=\"text-align: right;\">" + registrador.dado + "</td></tr>");
			}
			
			var tabelaInstrucoes = $("#tabelaInstrucoes").find("tbody");
			for (var nomeInstrucao in contexto.instrucoes) {
				var instrucao = contexto.instrucoes[nomeInstrucao];
				
				tabelaInstrucoes.append("<tr><td>" + instrucao + "</td></tr>")
			}
		}
	},
	
	definirEstadoPool: function() {
		if (null != MaquinaUniversal.contexto) {
			var status = MaquinaUniversal.contexto.status;
			
			if (status != "Finalizado" && status != "Tempo excedido") {
				MaquinaUniversal.podeExecutarPool = true;
				return;
			}
		}
		
		MaquinaUniversal.podeExecutarPool = false;
	},
	
	poolAtualizarCamposContexto: function () {
		if (MaquinaUniversal.podeExecutarPool) {
			MaquinaUniversal.atualizarContexto();
		}
	},
	
	limparTabelasContexto: function() {
		var tabela = $("#tabelaRegistradores").find("tbody");
		tabela.children().remove();
		
		tabela = $("#tabelaInstrucoes").find("tbody");
		tabela.children().remove();
	}
}

function adicionarEspacosNovaLinha(e) {
	if (e.keyCode == 13) {
		var codigo = $('#codigo');
		var posicaoAtual = codigo.prop("selectionStart");
		var sub = codigo.val().substr(0, posicaoAtual);
		
		var indice = posicaoAtual;
		var charAtual = null;
		while (indice > 0 && charAtual != "\n") {
			indice--;
			charAtual = sub[indice];
		}
		
		if (charAtual == "\n"  || indice == 0) {
			var quantidadeEspacos = 0;
			
			if (indice > 0) { // Se for a primeira linha, indice Ã© zero.
				indice++;
				charAtual = sub[indice];
				while (indice <= posicaoAtual && charAtual == " ") {
					quantidadeEspacos++;
					indice++;
					charAtual = sub[indice];
				}
			}
			
			if (isNovoBlocoInstrucao(sub)) {
				quantidadeEspacos += 2;
			}
			
			if (quantidadeEspacos > 0) {
				var novaString = sub + "\n";
				var i = 0;
				
				for (i = 0; i < quantidadeEspacos; i++) {
					novaString += " ";
				}
				
				novaString += codigo.val().substr(posicaoAtual);
				codigo.val(novaString);
				
				codigo[0].selectionEnd = posicaoAtual + quantidadeEspacos + 1;
				return false;
			}
		}
	}
}

function isNovoBlocoInstrucao(texto) {
	var sub = texto.trim();
	
	return sub[sub.length - 1] == "{";
}

$(document).ready(function(){
	$('.sidenav').sidenav();
    $('.modal').modal();
    
    MaquinaUniversal.limparTabelasContexto();
    
	setInterval(MaquinaUniversal.poolAtualizarCamposContexto, 3000);
});
