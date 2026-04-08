package br.com.observaacao.model.solicitacao;

import br.com.observaacao.model.EntityGenerico;
import br.com.observaacao.model.enums.NivelPrioridade;
import br.com.observaacao.model.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class Solicitacao extends EntityGenerico {

    private Long id;
    private Long id_categoria;
    private Long id_solicitante;
    private Long id_atendente;
    private Long id_endereco;
    private StatusSolicitacao status;
    private NivelPrioridade prioridade;
    private Boolean anonimo;
    private String titulo;
    private String descricao;
    private LocalDateTime dt_solicitada;
    private LocalDateTime dt_prazo;
    private LocalDateTime dt_finalizada;

    public Solicitacao(Long id_categoria, Long id_solicitante, Long id_atendente, Long id_endereco,
                       StatusSolicitacao status, NivelPrioridade prioridade, Boolean anonimo,
                       String titulo, String descricao, LocalDateTime dt_solicitada,
                       LocalDateTime dt_prazo, LocalDateTime dt_finalizada) {
        this.id_categoria = id_categoria;
        this.id_solicitante = id_solicitante;
        this.id_atendente = id_atendente;
        this.id_endereco = id_endereco;
        this.status = status;
        this.prioridade = prioridade;
        this.anonimo = anonimo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dt_solicitada = dt_solicitada;
        this.dt_prazo = dt_prazo;
        this.dt_finalizada = dt_finalizada;
    }

    public Solicitacao(Long id, Long id_categoria, Long id_solicitante, Long id_atendente,
                       Long id_endereco, StatusSolicitacao status, NivelPrioridade prioridade,
                       Boolean anonimo, String titulo, String descricao,
                       LocalDateTime dt_solicitada, LocalDateTime dt_prazo,
                       LocalDateTime dt_finalizada) {
        this.id = id;
        this.id_categoria = id_categoria;
        this.id_solicitante = id_solicitante;
        this.id_atendente = id_atendente;
        this.id_endereco = id_endereco;
        this.status = status;
        this.prioridade = prioridade;
        this.anonimo = anonimo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dt_solicitada = dt_solicitada;
        this.dt_prazo = dt_prazo;
        this.dt_finalizada = dt_finalizada;
    }

    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getId_categoria() { return id_categoria; }
    public void setId_categoria(Long id_categoria) { this.id_categoria = id_categoria; }

    public Long getId_solicitante() { return id_solicitante; }
    public void setId_solicitante(Long id_solicitante) { this.id_solicitante = id_solicitante; }

    public Long getId_atendente() { return id_atendente; }
    public void setId_atendente(Long id_atendente) { this.id_atendente = id_atendente; }

    public Long getId_endereco() { return id_endereco; }
    public void setId_endereco(Long id_endereco) { this.id_endereco = id_endereco; }

    public StatusSolicitacao getStatus() { return status; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }

    public NivelPrioridade getPrioridade() { return prioridade; }
    public void setPrioridade(NivelPrioridade prioridade) { this.prioridade = prioridade; }

    public Boolean getAnonimo() { return anonimo; }
    public void setAnonimo(Boolean anonimo) { this.anonimo = anonimo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDt_solicitada() { return dt_solicitada; }
    public void setDt_solicitada(LocalDateTime dt_solicitada) { this.dt_solicitada = dt_solicitada; }

    public LocalDateTime getDt_prazo() { return dt_prazo; }
    public void setDt_prazo(LocalDateTime dt_prazo) { this.dt_prazo = dt_prazo; }

    public LocalDateTime getDt_finalizada() { return dt_finalizada; }
    public void setDt_finalizada(LocalDateTime dt_finalizada) { this.dt_finalizada = dt_finalizada; }
}