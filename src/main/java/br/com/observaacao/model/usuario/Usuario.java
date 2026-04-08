package br.com.observaacao.model.usuario;

import br.com.observaacao.model.EntityGenerico;
import br.com.observaacao.model.enums.TipoUsuario;

import java.time.LocalDateTime;

public class Usuario extends EntityGenerico {

    private Long id;
    private Long criadoPor;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario;
    private final LocalDateTime dataCriacao;
    private boolean ativo;

    public Usuario(Long criadoPor, String nome, String cpf, String email, String senha, TipoUsuario tipoUsuario) {
        this.criadoPor = criadoPor;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public Usuario(Long id, Long criadoPor, String nome, String cpf, String email, String senha,
                   TipoUsuario tipoUsuario, LocalDateTime dataCriacao, boolean ativo) {
        this.id = id;
        this.criadoPor = criadoPor;
        this.nome = nome;
        this.cpf = cpf;
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

    public Long getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Long criadoPor) {
        this.criadoPor = criadoPor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
