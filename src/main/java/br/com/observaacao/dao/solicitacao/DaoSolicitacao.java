package br.com.observaacao.dao.solicitacao;

import br.com.observaacao.dao.DaoBase;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;

import java.util.List;

public interface DaoSolicitacao extends DaoBase<Solicitacao> {
    List<Solicitacao> listaPorUsuario(Long idUsuario);

    List<Solicitacao> buscarSolicitacaoEspecifica(StatusSolicitacao statusSolicitacao);

    List<Solicitacao> listaPorAtendente(Long idAtendente);

    void atualizar(Solicitacao entidade);
}
