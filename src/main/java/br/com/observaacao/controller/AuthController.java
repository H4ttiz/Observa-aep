package br.com.observaacao.controller;

import br.com.observaacao.controller.cidadao.MenuCidadaoController;
import br.com.observaacao.dto.usuario.UsuarioCadastroDto;
import br.com.observaacao.dto.usuario.UsuarioLoginDto;
import br.com.observaacao.model.usuario.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.usuario.ServiceUsuario;

import java.util.Scanner;



public class AuthController {

    private final ServiceUsuario service;
    private final Scanner sc = new Scanner(System.in);

    public AuthController(ServiceUsuario service) {
        this.service = service;
    }

    public void menuInicial() {

        while (true) {

            System.out.println("\n=== SISTEMA ===");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastro");
            System.out.println("0 - Sair");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> login();
                case 2 -> cadastro();
                case 0 -> System.exit(0);
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void cadastro() {

        try {

            System.out.println("\n=== CADASTRO ===");

            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("CPF: ");
            String cpf = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Senha: ");
            String senha = sc.nextLine();

            UsuarioCadastroDto dto =
                    new UsuarioCadastroDto(nome, cpf, email, senha, null);

            Usuario usuario = service.cadastroNormal(dto, TipoUsuario.C);

            System.out.println("Cadastro realizado com sucesso!");

            redirecionarMenu(usuario);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {

        try {

            System.out.println("\n==== LOGIN ====");

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Senha: ");
            String senha = sc.nextLine();

            UsuarioLoginDto dto =
                    new UsuarioLoginDto(email, senha);

            Usuario usuario = service.login(dto);

            redirecionarMenu(usuario);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void redirecionarMenu(Usuario usuario) {

        switch (usuario.getTipoUsuario()) {

            case C -> new MenuCidadaoController().menu(usuario);

            case S -> System.out.println("Menu funcionário (em construção)");

            case G -> System.out.println("Menu Gestor (em construção)");

            case A -> System.out.println("Menu admin (em construção)");
        }
    }
}
