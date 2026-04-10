package br.com.observaacao.service.historico_movimentacao_solicitacao;

import br.com.observaacao.dao.historico_movimentacao_solicitacao.DaoHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;

import java.util.List;

public class ServiceHistoricoMovimentacaoSolicitacao {

    private final DaoHistoricoMovimentacaoSolicitacao daoHistoricoMovimentacaoSolicitacao;

    public ServiceHistoricoMovimentacaoSolicitacao(DaoHistoricoMovimentacaoSolicitacao daoHistoricoMovimentacaoSolicitacao) {
        this.daoHistoricoMovimentacaoSolicitacao = daoHistoricoMovimentacaoSolicitacao;
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
