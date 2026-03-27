package br.com.observaacao.model.categoria;

import br.com.observaacao.model.EntityGenerico;

import java.time.LocalDateTime;

public class Categoria extends EntityGenerico {

    private Long id;
    private String categoria;
    private String descricao;
    private NivelPrioridade nivelPrioridade;
    private final LocalDateTime dataCriacao;
    private boolean ativo;

    public Categoria(String categoria, String descricao) {
        this.categoria = categoria;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public Categoria(Long id, String categoria, String descricao, LocalDateTime dataCriacao, boolean ativo) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public Long getId() {
        return 0L;
    }
}
