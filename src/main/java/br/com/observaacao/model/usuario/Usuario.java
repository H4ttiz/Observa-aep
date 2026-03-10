package br.com.observaacao.model.usuario;

import br.com.observaacao.model.EntityGenerico;

import java.time.LocalDateTime;

public class Usuario extends EntityGenerico {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;
    private final LocalDateTime dataCriacao;
    private boolean ativo;

    public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public Usuario(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario, LocalDateTime dataCriacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
