package br.com.observaacao.dao.solicitacao;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;

import java.util.List;

public interface DaoSolicitacao extends DaoGenerico<Solicitacao> {
    List<Solicitacao> listaPorUsuario(Long idUsuario);

    List<Solicitacao> buscarSolicitacaoEspecifica(StatusSolicitacao statusSolicitacao);

    List<Solicitacao> listaPorAtendente(Long idAtendente);
}
