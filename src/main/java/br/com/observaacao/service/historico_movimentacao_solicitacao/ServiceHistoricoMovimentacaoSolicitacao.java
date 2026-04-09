package br.com.observaacao.service.historico_movimentacao_solicitacao;

import br.com.observaacao.dao.historico_movimentacao_solicitacao.DaoHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;

import java.util.List;

public class ServiceHistoricoMovimentacaoSolicitacao {

    private final DaoHistoricoMovimentacaoSolicitacao daoHistoricoMovimentacaoSolicitacao;
    private final DaoSolicitacao daoSolicitacao;

    public ServiceHistoricoMovimentacaoSolicitacao(DaoHistoricoMovimentacaoSolicitacao daoHistoricoMovimentacaoSolicitacao,
                                                   DaoSolicitacao daoSolicitacao) {
        this.daoHistoricoMovimentacaoSolicitacao = daoHistoricoMovimentacaoSolicitacao;
        this.daoSolicitacao = daoSolicitacao;
    }

    public void addHistoricoMovimentacaoSolicitacao(Long idSolicitacao, StatusSolicitacao statusAnterior, StatusSolicitacao statusNovo, Long idResponsavel, String observacao) {

        HistoricoMovimentacaoSolicitacao historico = new HistoricoMovimentacaoSolicitacao(
                idSolicitacao,
                idResponsavel,
                observacao,
                statusNovo,
                statusAnterior
        );

        daoHistoricoMovimentacaoSolicitacao.salvar(historico);
    }

    public List<HistoricoMovimentacaoSolicitacao> listarPorSolicitacao(Long idSolicitacao) {
        if (idSolicitacao == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }
        return daoHistoricoMovimentacaoSolicitacao.buscarPorSolicitacao(idSolicitacao);
    }
}
