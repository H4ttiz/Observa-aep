package br.com.observaacao.app;

import br.com.observaacao.dao.categoria.DaoCategoria;
import br.com.observaacao.dao.categoria.DaoCategoriaImpl;
import br.com.observaacao.dao.endereco.DaoEndereco;
import br.com.observaacao.dao.endereco.DaoEnderecoImpl;
import br.com.observaacao.dao.historico_movimentacao_solicitacao.DaoHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.dao.historico_movimentacao_solicitacao.DaoHistoricoMovimentacaoSolicitacaoImpl;
import br.com.observaacao.dao.log.DaoLog;
import br.com.observaacao.dao.log.DaoLogImpl;
import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.dao.solicitacao.DaoSolicitacaoImpl;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.historico_movimentacao_solicitacao.ServiceHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.service.log.ServiceLog;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.view.AuthView;
import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dao.usuario.DaoUsuarioImpl;
import br.com.observaacao.service.usuario.ServiceUsuario;
public class Main{
    public static void main(String[] args) {

        // ===== LOG =====
        DaoLog daoLog = new DaoLogImpl();
        ServiceLog serviceLog = new ServiceLog(daoLog);

        // ===== USUÁRIO =====
        DaoUsuario daoUsuario = new DaoUsuarioImpl();
        ServiceUsuario serviceUsuario = new ServiceUsuario(daoUsuario,serviceLog);

        // ===== ENDEREÇO =====
        DaoEndereco daoEndereco = new DaoEnderecoImpl();
        ServiceEndereco serviceEndereco = new ServiceEndereco(daoEndereco,serviceLog);

        // ===== HISTORICO | SOLICITAÇÃO =====
        DaoHistoricoMovimentacaoSolicitacao daoHistorico = new DaoHistoricoMovimentacaoSolicitacaoImpl();
        DaoSolicitacao daoSolicitacao = new DaoSolicitacaoImpl();
        ServiceHistoricoMovimentacaoSolicitacao serviceHistorico = new ServiceHistoricoMovimentacaoSolicitacao(daoHistorico,daoSolicitacao);
        ServiceSolicitacao serviceSolicitacao = new ServiceSolicitacao(daoSolicitacao,serviceHistorico,serviceLog);

        // ===== CATEGORIA =====
        DaoCategoria daoCategoria = new DaoCategoriaImpl();
        ServiceCategoria serviceCategoria = new ServiceCategoria(daoCategoria,daoUsuario,serviceLog);

        // ===== VIEW =====
        AuthView authView = new AuthView(
                serviceUsuario,
                serviceEndereco,
                serviceSolicitacao,
                serviceCategoria,
                serviceHistorico,
                serviceLog
        );

        authView.menuInicial();
    }
}
