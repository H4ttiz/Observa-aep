package br.com.observaacao.view;

import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;
import br.com.observaacao.view.cidadao.MenuCidadaoView;
import br.com.observaacao.dto.usuario.UsuarioCadastroDto;
import br.com.observaacao.dto.usuario.UsuarioLoginDto;
import br.com.observaacao.model.enums.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.view.gestor.MenuGestorView;

import java.util.Scanner;

public class AuthView {
    private final Scanner sc = new Scanner(System.in);

    private final ServiceUsuario service;
    private final ServiceEndereco serviceEndereco;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceCategoria serviceCategoria;

    public AuthView(ServiceUsuario service,
                    ServiceEndereco serviceEndereco,
                    ServiceSolicitacao serviceSolicitacao,
                    ServiceCategoria serviceCategoria) {
        this.service = service;
        this.serviceEndereco = serviceEndereco;
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceCategoria = serviceCategoria;
    }

    public void menuInicial() {
        while (true) {
            System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃           SISTEMA OBSERVAÇÃO AEP            ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.println("  " + Cores.AZUL + "[ SEJA BEM-VINDO ]" + Cores.RESET);
            System.out.println("  " + Cores.CIANO + "1." + Cores.RESET + " Acessar Minha Conta (Login)");
            System.out.println("  " + Cores.CIANO + "2." + Cores.RESET + " Criar Novo Cadastro");
            System.out.println("  " + Cores.CIANO + "0." + Cores.RESET + " Sair do Sistema");
            System.out.println(Cores.CIANO + "  ─────────────────────────────────────────────" + Cores.RESET);

            System.out.print(Cores.AMARELO + "  ▸ Escolha uma opção: " + Cores.RESET);

            int opcao;
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(Cores.VERMELHO + "    ⚠ Digite um número válido!" + Cores.RESET);
                continue;
            }

            switch (opcao) {
                case 1 -> login();
                case 2 -> cadastro();
                case 0 -> {
                    System.out.println(Cores.AMARELO + "\n  Encerrando atividades..." + Cores.RESET);
                    Loading.executar("Desconectando");
                    System.exit(0);
                }
                default -> System.out.println(Cores.VERMELHO + "    ⚠ Opção inválida!" + Cores.RESET);
            }
        }
    }

    private void cadastro() {
        try {
            System.out.println("\n" + Cores.CIANO + "  [ FORMULÁRIO DE CADASTRO ]" + Cores.RESET);

            System.out.print("  ▸ Nome Completo: ");
            String nome = sc.nextLine();

            System.out.print("  ▸ CPF (apenas números): ");
            String cpf = sc.nextLine();

            System.out.print("  ▸ E-mail: ");
            String email = sc.nextLine();

            System.out.print("  ▸ Senha: ");
            String senha = sc.nextLine();

            Loading.executar("Processando registro");

            UsuarioCadastroDto dto = new UsuarioCadastroDto(nome, cpf, email, senha, null);
            Usuario usuario = service.cadastroNormal(dto, TipoUsuario.C);

            System.out.println("\n" + Cores.VERDE + "  ✔ Conta criada com sucesso!" + Cores.RESET);
            Loading.executar("Preparando seu ambiente");

            redirecionarMenu(usuario);

        } catch (RuntimeException e) {
            System.out.println(Cores.VERMELHO + "\n  ⚠ Erro no cadastro: " + e.getMessage() + Cores.RESET);
        }
    }

    private void login() {
        try {
            System.out.println("\n" + Cores.CIANO + "  [ ACESSO AO SISTEMA ]" + Cores.RESET);

            System.out.print("  ▸ E-mail: ");
            String email = sc.nextLine();

            System.out.print("  ▸ Senha: ");
            String senha = sc.nextLine();

            Loading.executar("Verificando credenciais");

            UsuarioLoginDto dto = new UsuarioLoginDto(email, senha);
            Usuario usuario = service.login(dto);

            System.out.println("\n" + Cores.VERDE + "  ✔ Autenticação concluída!" + Cores.RESET);
            Loading.executar("Carregando perfil");

            redirecionarMenu(usuario);

        } catch (RuntimeException e) {
            System.out.println(Cores.VERMELHO + "\n  ⚠ Falha no login: " + e.getMessage() + Cores.RESET);
        }
    }

    private void redirecionarMenu(Usuario usuario) {
        switch (usuario.getTipoUsuario()) {
            case C -> new MenuCidadaoView(serviceEndereco, serviceSolicitacao, serviceCategoria)
                    .menu(usuario);

            case S -> System.out.println(Cores.AMARELO + "\n  [!] Módulo Servidor em desenvolvimento." + Cores.RESET);

            case G -> new MenuGestorView(serviceSolicitacao,service,serviceEndereco)
                    .menu(usuario);

            case A -> System.out.println(Cores.AMARELO + "\n  [!] Módulo Administrador em desenvolvimento." + Cores.RESET);
        }
    }
}