package br.com.observaacao.view.atendente;

import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;
import br.com.observaacao.view.AlterarSenhaView;

import java.util.List;
import java.util.Scanner;

public class MenuAtendenteView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceUsuario serviceUsuario;
    private final ServiceEndereco serviceEndereco;

    public MenuAtendenteView(ServiceSolicitacao serviceSolicitacao, ServiceUsuario serviceUsuario, ServiceEndereco serviceEndereco) {
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceUsuario = serviceUsuario;
        this.serviceEndereco = serviceEndereco;
    }

    public void menu(Usuario atendente) {
        while (true) {
            try {
                System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
                System.out.printf("  ┃  👷 %-39s \n", Cores.VERDE + atendente.getNome().toUpperCase() + Cores.CIANO);
                System.out.printf("  ┃  🔰 %-39s \n", Cores.RESET + "PERFIL: ATENDENTE TÉCNICO" + Cores.CIANO);
                System.out.printf("  ┃  🆔 %-39s \n", Cores.RESET + "CPF: " + atendente.getCpf() + Cores.CIANO);
                System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

                System.out.println("  " + Cores.AZUL + "[ FILA DE TRABALHO ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "1." + Cores.RESET + " Listar Demandas Disponíveis (N3)");
                System.out.println("  " + Cores.CIANO + "2." + Cores.RESET + " Vincular-se a uma Solicitação (Puxar)");

                System.out.println("\n  " + Cores.AZUL + "[ MINHA OPERAÇÃO ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "3." + Cores.RESET + " Ver Meus Atendimentos");
                System.out.println("  " + Cores.CIANO + "4." + Cores.RESET + " Atualizar Diário de Bordo (Observações)");
                System.out.println("  " + Cores.CIANO + "5." + Cores.RESET + " Finalizar Solicitação (N5)");

                System.out.println("\n  " + Cores.AZUL + "[ PESSOAL ]" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "6." + Cores.RESET + " Trocar Senha");
                System.out.println("\n  " + Cores.CIANO + "0." + Cores.RESET + " Encerrar Sessão (Logout)");
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
                    Loading.executar("Finalizando painel técnico");
                    return;
                }

                AtendimentoView atendimentoView = new AtendimentoView(serviceSolicitacao);
                AlterarSenhaView alterarSenhaView = new AlterarSenhaView(serviceUsuario);
                switch (opcao) {
                    case 1 -> listarDisponiveis();
                    case 2 -> atendimentoView.puxarSolicitacao(atendente);
                    case 3 -> listarMeusAtendimentos(atendente);
                    case 4 -> atendimentoView.atualizarAndamento(atendente);
                    case 5 -> atendimentoView.finalizarSolicitacao(atendente);
                    case 6 -> alterarSenhaView.exibirTela(atendente);
                    default -> System.out.println(Cores.VERMELHO + "    ⚠ Opção inválida!" + Cores.RESET);
                }

            } catch (Exception e) {
                System.out.println(Cores.VERMELHO + "    ⚠ Erro inesperado: " + e.getMessage() + Cores.RESET);
            }
        }
    }

    private void listarDisponiveis() {
        System.out.println("\n" + Cores.AMARELO + " [ DEMANDAS AGUARDANDO EQUIPE - STATUS N3 ] " + Cores.RESET);
        List<Solicitacao> solicitacaos = serviceSolicitacao.buscarSolicitacaoAguardandoAtendimento();

        if (solicitacaos.isEmpty()) {
            System.out.println("  ℹ Nenhuma demanda aguardando atendimento no momento.");
            return;
        }

        for (Solicitacao solicitacao : solicitacaos) {
            imprimirCardParaAtendente(solicitacao);
        }
    }

    private void listarMeusAtendimentos(Usuario atendente) {
        System.out.println("\n" + Cores.ROXO + " [ MEUS SERVIÇOS EM ABERTO - STATUS N4 ] " + Cores.RESET);
        List<Solicitacao> solicitacaos = serviceSolicitacao.buscarPorAtendente(atendente.getId());

        if (solicitacaos.isEmpty()) {
            System.out.println("  ℹ Você não possui atendimentos vinculados no momento.");
            return;
        }

        for (Solicitacao solicitacao : solicitacaos) {
            imprimirCardParaAtendente(solicitacao);
        }
    }

    private void imprimirCardParaAtendente(Solicitacao solicitacao) {
        boolean vencido = solicitacao.getDt_prazo() != null && solicitacao.getDt_prazo().isBefore(java.time.LocalDateTime.now());
        String corCard = vencido ? Cores.VERMELHO : Cores.CIANO;
        String corPrazo = vencido ? Cores.VERMELHO : Cores.VERDE;

        System.out.println("\n" + corCard + "  ID: #" + solicitacao.getId() + " - " + solicitacao.getTitulo().toUpperCase() + Cores.RESET);

        System.out.println("  ┃ Prioridade: " + (solicitacao.getPrioridade() != null ? solicitacao.getPrioridade().getPrioridade() : "N/A"));
        if (solicitacao.getDt_prazo() != null) {
            System.out.println("  ┃ Prazo:      " + corPrazo + solicitacao.getDt_prazo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + (vencido ? " [ATRASADO]" : "") + Cores.RESET);
        }

        System.out.print("  ┃ Solicitante: ");
        if (solicitacao.getAnonimo()) {
            System.out.println(Cores.AMARELO + "ANÔNIMO" + Cores.RESET);
        } else {
            try {
                Usuario u = serviceUsuario.buscarPorId(solicitacao.getId_solicitante());
                System.out.println(Cores.VERDE + u.getNome() + Cores.RESET);
                System.out.println("  ┃   📧 Email: " + u.getEmail() + " | 🆔 CPF: " + u.getCpf());
            } catch (Exception e) {
                System.out.println("Dados do solicitante indisponíveis.");
            }
        }

        try {
            var end = serviceEndereco.buscarPorId(solicitacao.getId_endereco());
            System.out.println("  ┃ Local:      " + end.getLogradouro() + ", " + end.getNumero() + " - " + end.getBairro());
        } catch (Exception e) {
            System.out.println("  ┃ Local:      Endereço não localizado.");
        }

        System.out.println("  ┃ Descrição:  " + solicitacao.getDescricao());
        if (solicitacao.getObservacao() != null && !solicitacao.getObservacao().isBlank()) {
            System.out.println("  ┃ Histórico:  " + Cores.AMARELO + solicitacao.getObservacao() + Cores.RESET);
        }

        System.out.println("  ┗" + corCard + "────────────────────────────────────────────────────────" + Cores.RESET);
    }
}
