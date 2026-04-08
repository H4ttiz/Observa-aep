package br.com.observaacao.model.enums;

public enum TipoUsuario {
    C("Cidadão"),
    S("Servidor Público"),
    G("Gestor"),
    A("Administrador");

    private String descricao;

    TipoUsuario(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
