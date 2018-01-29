$(function () {
    $('[data-toggle="tooltip"]').tooltip();

    var botoesFinalizar = $('.js-finalizar');
    botoesFinalizar.on('click', finalizar);

    $("#filtro").on("change", function () {
        window.location.href = "/tarefas?filtro=" + this.value;
    });

    eventoExcluir();

});

function eventoExcluir() {
    var botoesExclusao = $('.js-excluir');
    botoesExclusao.on('click', remover);
}

function remover(evento) {
    var botaoClicado = $(evento.currentTarget);
    var td = botaoClicado.closest('td');
    var tr = botaoClicado.closest('tr');
    tr.addClass("trExcluindo");
    td.html("<i class=\"fa fa-spinner fa-spin fa-fw\"></i>");
    var url = botaoClicado.data('url') + botaoClicado.data('id');
    $.ajax({
        url: url,
        method: 'DELETE',
        success: function () {
            tr.fadeOut(200, function () {
                tr.remove();
            });
        },
        error: function (e) {
            alert('Erro ao excluir tarefa: ' + e.responseText);
        }
    });
}

function finalizar(evento) {
    var botaoClicado = $(evento.currentTarget);
    var tr = botaoClicado.closest('tr');
    var td = botaoClicado.closest('td');
    tr.addClass("trFinalizando");
    td.html("<i class=\"fa fa-spinner fa-spin fa-fw\"></i>");
    var url = botaoClicado.data('url') + botaoClicado.data('id');
    $.ajax({
        url: url,
        method: 'GET',
        success: function (e) {
            if ($('#filtro').val() == 3) {
                var urlAux = "/tarefas/";
                var tarefa = e;
                var opcoes = "<a class=\"js-excluir\" title=\"Excluir\" href=\"#\" data-id=\"" + tarefa.id + "\" data-url=\"" + urlAux + "\" data-toggle=\"tooltip\"><i class=\"fa fa-trash\"></i></a>";
                tr.html("<th>" + tarefa.id + "</th>" +
                        "<td>" + tarefa.descricao + "</td>" +
                        "<td class='situacao'><span class='btn btn-xs " + (tarefa.finalizado ? "btn-success" : "btn-warning") + "'>" + tarefa.situacao + "</span></td>" +
                        "<td class=\"opcoes\">" + opcoes + "</td>");
                eventoExcluir();
            } else {
                tr.fadeOut(200, function () {
                    tr.remove();
                });
            }
        },
        error: function (e) {
            alert('Erro ao finalizar tarefa: ' + e.responseText);
        }
    });
}


