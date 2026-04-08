package br.com.observaacao.model.enums;

public enum StatusSolicitacao {

    N1("Aguardando Aprovaçao"),
    N2("Aprovada"),
    N3("Aguardando Atendimento"),
    N4("Em Andamento"),
    N5("Finalizada"),
    N6("Rejeitada");

    private String status;

    StatusSolicitacao(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
