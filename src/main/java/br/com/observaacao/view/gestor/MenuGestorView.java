package br.com.observaacao.view.gestor;

import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;
import java.util.Scanner;

public class MenuGestorView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceUsuario serviceUsuario;
    private final ServiceEndereco serviceEndereco;

    public MenuGestorView(ServiceSolicitacao serviceSolicitacao, ServiceUsuario serviceUsuario, ServiceEndereco serviceEndereco) {
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceUsuario = serviceUsuario;
        this.serviceEndereco = serviceEndereco;
    }

    public void menu(Usuario usuario) {

        while (true) {
            try {
                System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
                System.out.printf("  ┃  💼 %-39s \n", Cores.VERDE + usuario.getNome().toUpperCase() + Cores.CIANO);
                System.out.printf("  ┃  🔰 %-39s\n", Cores.RESET + "PERFIL: GESTOR PÚBLICO" + Cores.CIANO);
                System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

                System.out.println("  " + Cores.AZUL + "[ PAINEL DE CONTROLE ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "1." + Cores.RESET + " Listar Todas as Solicitações");
                System.out.println("  " + Cores.CIANO + "2." + Cores.RESET + " Ver Pendentes de Aprovação");
                System.out.println("  " + Cores.CIANO + "3." + Cores.RESET + " Aprovar Solicitação (Prioridade/Prazo)");
                System.out.println("  " + Cores.CIANO + "4." + Cores.RESET + " Rejeitar Solicitação");
                System.out.println("  " + Cores.CIANO + "5." + Cores.RESET + " Acompanhar Andamento das Equipes");
                System.out.println("  " + Cores.CIANO + "6." + Cores.RESET + " Ver Solicitações Atrasadas");
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

                processarOpcao(opcao, usuario);

            } catch (Exception e) {
                System.out.println(Cores.VERMELHO + "    ⚠ Erro inesperado: " + e.getMessage() + Cores.RESET);
                aguardar();
            }
        }
    }

    private void processarOpcao(int opcao, Usuario usuario) {
        SolicitacaoGestorView telaLista = new SolicitacaoGestorView(serviceSolicitacao,serviceUsuario,serviceEndereco);
        DecisaoGestorView telaDecisao = new DecisaoGestorView(serviceSolicitacao);
        MonitoramentoGestorView telaMonitor = new MonitoramentoGestorView(serviceSolicitacao);

        switch (opcao) {
            case 1 -> {
                Loading.executar("Buscando registros");
                telaLista.exibirTodas();
            }
            case 2 -> {
                Loading.executar("Filtrando pendências");
                telaLista.exibirPendentes();
            }
            case 3 -> {
                telaDecisao.aprovar(usuario);
            }
            case 4 -> {
                telaDecisao.rejeitar(usuario);
            }
            case 5 -> {
                Loading.executar("Sincronizando andamento");
                telaMonitor.verAndamento();
            }
            case 6 -> {
                Loading.executar("Analisando prazos");
                telaMonitor.verAtrasadas();
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