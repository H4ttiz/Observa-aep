package br.com.observaacao.view.adm;

import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.log.ServiceLog;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;
import br.com.observaacao.view.AlterarSenhaView;

import java.util.Scanner;

public class MenuAdmView {
    private final Scanner sc = new Scanner(System.in);
    private final ServiceUsuario serviceUsuario;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceCategoria serviceCategoria;
    private final ServiceLog serviceLog;

    public MenuAdmView(ServiceUsuario serviceUsuario, ServiceSolicitacao serviceSolicitacao,
                       ServiceCategoria serviceCategoria, ServiceLog serviceLog) {
        this.serviceUsuario = serviceUsuario;
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceCategoria = serviceCategoria;
        this.serviceLog = serviceLog;
    }

    public void menu(Usuario admin){
        while (true) {
            try {
                System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
                System.out.printf("  ┃  💼 %-39s \n", Cores.VERDE + admin.getNome().toUpperCase() + Cores.CIANO);
                System.out.printf("  ┃  🔰 %-39s\n", Cores.RESET + "PERFIL: Administrador" + Cores.CIANO);
                System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

                System.out.println("  " + Cores.AZUL + "[ PAINEL DE CONTROLE ]" + Cores.RESET);

                System.out.println("\n  " + Cores.AZUL + "[ USUARIOS ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "1." + Cores.RESET + " Listar Usuarios");
                System.out.println("  " + Cores.CIANO + "2." + Cores.RESET + " Cadastrar Usuario");
                System.out.println("  " + Cores.CIANO + "3." + Cores.RESET + " Ativar Usuario");
                System.out.println("  " + Cores.CIANO + "4." + Cores.RESET + " Desativar Usuario");

                System.out.println("\n  " + Cores.AZUL + "[ CATEGORIAS ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "5." + Cores.RESET + " Listar Categoria");
                System.out.println("  " + Cores.CIANO + "6." + Cores.RESET + " Cadastrar Categoria");
                System.out.println("  " + Cores.CIANO + "7." + Cores.RESET + " Atualizar Categoria");
                System.out.println("  " + Cores.CIANO + "8." + Cores.RESET + " Ativar Categoria");
                System.out.println("  " + Cores.CIANO + "9." + Cores.RESET + " Desativar Categoria");

                System.out.println("\n  " + Cores.AZUL + "[ SIGILOSAS ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "10." + Cores.RESET + " Ver Log");
                System.out.println("  " + Cores.CIANO + "11." + Cores.RESET + " Visualização Sigilosa (Acesso Restrito)");

                System.out.println("\n  " + Cores.AZUL + "[ PESSOAL ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "12." + Cores.RESET + " Trocar Senha");
                System.out.println("  " + Cores.CIANO + "0." + Cores.RESET + " Encerrar Sessão (Logout)");
                System.out.println(Cores.CIANO + "  ─────────────────────────────────────────────" + Cores.RESET);

                System.out.print(Cores.AMARELO + "  ▸ Escolha uma opção: " + Cores.RESET);

                int opcao;
                try {
                    opcao = Integer.parseInt(sc.nextLine());
                } catch (Exception e) {
                    System.out.println(Cores.VERMELHO + "    ⚠ Digite um número válido!" + Cores.RESET);
                    continue;
                }

                if (opcao == 0) {
                    System.out.println(Cores.AMARELO + "    Saindo..." + Cores.RESET);
                    Loading.executar("Finalizando painel de gestão");
                    return;
                }

                processarOpcao(opcao, admin);

            } catch (Exception e) {
                System.out.println(Cores.VERMELHO + "    ⚠ Erro inesperado: " + e.getMessage() + Cores.RESET);
                aguardar();
            }
        }
    }

    private void processarOpcao(int opcao, Usuario admin) {

        GestaoUsuarioView gestaoUsuarioView = new GestaoUsuarioView(serviceUsuario);
        GestaoCategoriaView gestaoCategoriaView = new GestaoCategoriaView(serviceCategoria);
        GestaoSigiloView gestaoSigiloView = new GestaoSigiloView(serviceUsuario,serviceLog,serviceSolicitacao);
        AlterarSenhaView alterarSenhaView = new AlterarSenhaView(serviceUsuario);

        switch (opcao) {
            case 1 -> {
                Loading.executar("Buscando registros");
                gestaoUsuarioView.visualizarUsuarios(admin);
            }
            case 2 -> {
                Loading.executar("Preparando Formulario");
                gestaoUsuarioView.cadastrarUsuario(admin);
            }
            case 3 -> {
                Loading.executar("Buscando Usuarios");
                gestaoUsuarioView.ativarUsuario(admin);
            }
            case 4 -> {
                Loading.executar("Buscando Usuarios");
                gestaoUsuarioView.desativarUsuario(admin);
            }
            case 5 -> {
                Loading.executar("Buscando registros");
                gestaoCategoriaView.listarCategorias(admin);
            }
            case 6 -> {
                Loading.executar("Preparando Formulario");
                gestaoCategoriaView.cadastrarCategoria(admin);
            }
            case 7 -> {
                Loading.executar("Buscando Categorias");
                gestaoCategoriaView.editarCategoria(admin);
            }
            case 8 -> {
                Loading.executar("Buscando Categorias");
                gestaoCategoriaView.reativarCategoria(admin);
            }
            case 9 -> {
                Loading.executar("Buscando Categorias");
                gestaoCategoriaView.desativarCategoria(admin);
            }
            case 10 -> {
                Loading.executar("Buscando registros");
                gestaoSigiloView.visualizarlog(admin);
            }
            case 11 -> {
                Loading.executar("Descriptografando dados sensíveis");
                gestaoSigiloView.visualizarAnonimo(admin);
            }
            case 12 -> {
                Loading.executar("Buscando Dados");
                alterarSenhaView.exibirTela(admin);
            }
            default -> {
                System.out.println(Cores.VERMELHO + "    ⚠ Opção inválida!" + Cores.RESET);
                return;
            }
        }

        aguardar();
    }

    private void aguardar() {
        System.out.println("\n  " + Cores.CIANO + "Pressione ENTER para voltar ao menu..." + Cores.RESET);
        sc.nextLine();
    }
}
