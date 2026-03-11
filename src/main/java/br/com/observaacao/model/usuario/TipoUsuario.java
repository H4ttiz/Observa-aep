package br.com.observaacao.model.usuario;

public enum TipoUsuario {
    C("Cidadão"),
    S("Servidor Público"),
    G("Gestor");

    private String descricao;

    TipoUsuario(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
