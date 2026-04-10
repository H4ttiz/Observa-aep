package br.com.observaacao.dao.historico_movimentacao_solicitacao;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;

import java.util.List;

public interface DaoHistoricoMovimentacaoSolicitacao extends DaoGenerico<HistoricoMovimentacaoSolicitacao> {

    List<HistoricoMovimentacaoSolicitacao> buscarPorSolicitacao(Long idSolicitacao);
}
