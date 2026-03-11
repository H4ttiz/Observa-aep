package br.com.observaacao.app;

import br.com.observaacao.controller.AuthController;
import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dao.usuario.DaoUsuarioImpl;
import br.com.observaacao.service.usuario.ServiceUsuario;

public class Main {
    public static void main(String[] args) {

        DaoUsuario daoUsuario = new DaoUsuarioImpl();
        ServiceUsuario serviceUsuario = new ServiceUsuario(daoUsuario);

        AuthController authController = new AuthController(serviceUsuario);

        authController.menuInicial();
    }
}