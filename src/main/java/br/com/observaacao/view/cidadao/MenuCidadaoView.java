package br.com.observaacao.view.cidadao;

import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.CpfUtil;
import br.com.observaacao.util.Loading;

import java.util.Scanner;

public class MenuCidadaoView {
    private final Scanner sc = new Scanner(System.in);
    private final CpfUtil cpfUtil = new CpfUtil();

    private final ServiceEndereco serviceEndereco;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceCategoria serviceCategoria;

    public MenuCidadaoView(ServiceEndereco serviceEndereco,
                           ServiceSolicitacao serviceSolicitacao,
                           ServiceCategoria serviceCategoria) {
        this.serviceEndereco = serviceEndereco;
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceCategoria = serviceCategoria;
    }

    public void menu(Usuario usuario) {

        while (true) {
            System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.printf("  ┃  👤 %-39s \n", Cores.AMARELO + usuario.getNome().toUpperCase() + Cores.CIANO);
            System.out.printf("  ┃  🆔 %-39s\n", Cores.RESET + "CPF: " + cpfUtil.formatarCpf(usuario.getCpf()) + Cores.CIANO);
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.println("  " + Cores.AZUL + "[ MENU PRINCIPAL ]" + Cores.RESET);
            System.out.println("  " + Cores.CIANO + "1." + Cores.RESET + " Criar Nova Solicitação");
            System.out.println("  " + Cores.CIANO + "2." + Cores.RESET + " Ver Minhas Solicitações");
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

            switch (opcao) {
                case 1 -> {
                    Loading.executar("Iniciando formulário");
                    new SolicitacaoCidadaoView(serviceEndereco, serviceSolicitacao, serviceCategoria)
                            .criarSolicitacao(usuario);
                }

                case 2 -> {
                    Loading.executar("Buscando registros");
                    new SolicitacaoCidadaoView(serviceEndereco, serviceSolicitacao, serviceCategoria)
                            .visualizarSolicitacao(usuario);
                }

                case 0 -> {
                    System.out.println(Cores.AMARELO + "    Saindo..." + Cores.RESET);
                    Loading.executar("Limpando cache de sessão");
                    return;
                }

                default -> System.out.println(Cores.VERMELHO + "    ⚠ Opção inválida!" + Cores.RESET);
            }
        }
    }
}