package br.com.observaacao.app;

import br.com.observaacao.dao.categoria.DaoCategoria;
import br.com.observaacao.dao.categoria.DaoCategoriaImpl;
import br.com.observaacao.dao.endereco.DaoEndereco;
import br.com.observaacao.dao.endereco.DaoEnderecoImpl;
import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.dao.solicitacao.DaoSolicitacaoImpl;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.view.AuthView;
import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dao.usuario.DaoUsuarioImpl;
import br.com.observaacao.service.usuario.ServiceUsuario;
public class Main{
    public static void main(String[] args) {

        // ===== USUÁRIO =====
        DaoUsuario daoUsuario = new DaoUsuarioImpl();
        ServiceUsuario serviceUsuario = new ServiceUsuario(daoUsuario);

        // ===== ENDEREÇO =====
        DaoEndereco daoEndereco = new DaoEnderecoImpl();
        ServiceEndereco serviceEndereco = new ServiceEndereco(daoEndereco);

        // ===== SOLICITAÇÃO =====
        DaoSolicitacao daoSolicitacao = new DaoSolicitacaoImpl();
        ServiceSolicitacao serviceSolicitacao =
                new ServiceSolicitacao(daoSolicitacao);

        // ===== CATEGORIA =====
        DaoCategoria daoCategoria = new DaoCategoriaImpl();
        ServiceCategoria serviceCategoria = new ServiceCategoria(daoCategoria,daoUsuario);

        // ===== VIEW =====
        AuthView authView = new AuthView(
                serviceUsuario,
                serviceEndereco,
                serviceSolicitacao,
                serviceCategoria
        );

        authView.menuInicial();
    }
}
