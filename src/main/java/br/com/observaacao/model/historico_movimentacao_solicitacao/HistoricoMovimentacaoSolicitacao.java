package br.com.observaacao.model.historico_movimentacao_solicitacao;

import br.com.observaacao.model.EntityGenerico;
import br.com.observaacao.model.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class HistoricoMovimentacaoSolicitacao extends EntityGenerico {

    private Long id;
    private Long id_solicitacao;
    private Long id_responsavel;
    private String comentario;
    private StatusSolicitacao status_atual;
    private StatusSolicitacao status_anterior;
    private final LocalDateTime data_movimentacao;


    public HistoricoMovimentacaoSolicitacao(Long id_solicitacao, Long id_responsavel,
                                            String comentario, StatusSolicitacao status_atual,
                                            StatusSolicitacao status_anterior) {
        this.id_solicitacao = id_solicitacao;
        this.id_responsavel = id_responsavel;
        this.comentario = comentario;
        this.status_atual = status_atual;
        this.status_anterior = status_anterior;
        this.data_movimentacao = LocalDateTime.now();
    }

    public HistoricoMovimentacaoSolicitacao(Long id, Long id_solicitacao, Long id_responsavel,
                                            String comentario, StatusSolicitacao status_atual,
                                            StatusSolicitacao status_anterior, LocalDateTime data_movimentacao) {
        this.id = id;
        this.id_solicitacao = id_solicitacao;
        this.id_responsavel = id_responsavel;
        this.comentario = comentario;
        this.status_atual = status_atual;
        this.status_anterior = status_anterior;
        this.data_movimentacao = data_movimentacao;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(Long id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
    }

    public Long getId_responsavel() {
        return id_responsavel;
    }

    public void setId_responsavel(Long id_responsavel) {
        this.id_responsavel = id_responsavel;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public StatusSolicitacao getStatus_atual() {
        return status_atual;
    }

    public void setStatus_atual(StatusSolicitacao status_atual) {
        this.status_atual = status_atual;
    }

    public StatusSolicitacao getStatus_anterior() {
        return status_anterior;
    }

    public void setStatus_anterior(StatusSolicitacao status_anterior) {
        this.status_anterior = status_anterior;
    }

    public LocalDateTime getData_movimentacao() {
        return data_movimentacao;
    }
}
