function resetar() {
	var status = $('#statusExecucao')[0];
	status.innerText = "Não iniciado";
}

function atualizar() {
	var status = $('#statusExecucao')[0].innerText;
	
	if (status != "Finalizado" || status != "Tempo excedido") {
		atualizarEstado();
	}
}

setInterval(atualizar, 3000);

function adicionarEspacosNovaLinha(e) {
	if (e.keyCode == 13) {
		var codigo = $('#form\\:codigo');
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
			
			if (indice > 0) { // Se for a primeira linha, indice é zero.
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

