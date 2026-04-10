package br.com.observaacao.dao.historico_movimentacao_solicitacao;

import br.com.observaacao.dao.DaoBase;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;

import java.util.List;

public interface DaoHistoricoMovimentacaoSolicitacao extends DaoBase<HistoricoMovimentacaoSolicitacao> {

    List<HistoricoMovimentacaoSolicitacao> buscarPorSolicitacao(Long idSolicitacao);
}
