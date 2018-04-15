function resetar() {
	var status = $('#statusExecucao')[0];
	status.innerText = "Não iniciado";
}

function atualizar() {
	var status = $('#statusExecucao')[0].innerText;
	
	if (status != "Finalizado") {
		atualizarEstado();
	}
}

setInterval(atualizar, 3000);