package br.com.observaacao.model.enums;

public enum NivelPrioridade {

    N1("Baixa"),
    N2("Moderada"),
    N3("Média"),
    N4("Alta"),
    N5("Urgente");

    private String prioridade;

    NivelPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getPrioridade() {
        return prioridade;
    }
}