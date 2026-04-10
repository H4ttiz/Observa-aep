package br.com.observaacao.view.gestor;

import br.com.observaacao.model.endereco.Endereco;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.historico_movimentacao_solicitacao.ServiceHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.DataUtil;
import br.com.observaacao.util.Loading;

import java.util.List;
import java.util.Scanner;


public class MonitoramentoGestorView {
    private final Scanner sc = new Scanner(System.in);
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceUsuario serviceUsuario;
    private final ServiceHistoricoMovimentacaoSolicitacao serviceHistorico;
    private final ServiceEndereco serviceEndereco;

    public MonitoramentoGestorView(ServiceSolicitacao serviceSolicitacao, ServiceUsuario serviceUsuario,
                                   ServiceHistoricoMovimentacaoSolicitacao serviceHistorico,
                                   ServiceEndereco serviceEndereco) {
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceUsuario = serviceUsuario;
        this.serviceHistorico = serviceHistorico;
        this.serviceEndereco = serviceEndereco;
    }

    public void verAndamento() {
        System.out.println("\n" + Cores.ROXO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          SOLICITAÇÕES EM EXECUÇÃO           ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        Loading.executar("Mapeando equipes e atendentes");
        List<Solicitacao> lista = serviceSolicitacao.buscarSolicitacaoEmAndamento();

        if (lista.isEmpty()) {
            System.out.println(Cores.AMARELO + "    ℹ Não há solicitações em execução no momento." + Cores.RESET);
            return;
        }

        for (Solicitacao solicitacao : lista) {
            imprimirCardComAtendente(solicitacao);
        }
    }

    public void verAguardandoAtendimento() {
        System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃        AGUARDANDO INÍCIO DAS EQUIPES        ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        Loading.executar("Verificando fila de espera");
        List<Solicitacao> lista = serviceSolicitacao.buscarSolicitacaoAguardandoAtendimento(); // Status N3

        processarListagemMonitoramento(lista, "A fila de atendimento está vazia.");
    }

    public void verAtrasadas() {
        System.out.println("\n" + Cores.VERMELHO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃    ⚠️  ALERTA CRÍTICO: PRAZOS VENCIDOS       ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        Loading.executar("Analisando cronogramas e atrasos");
        List<Solicitacao> lista = serviceSolicitacao.buscarSolicitacaoAtrasadas();

        if (lista.isEmpty()) {
            System.out.println(Cores.VERDE + "    ✔ Parabéns! Não há solicitações atrasadas." + Cores.RESET);
            return;
        }

        for (Solicitacao solicitacao : lista) {
            imprimirCardMonitoramento(solicitacao, true);
        }
    }

    public void linhaDoTempoGestor() {
        try {
            System.out.println("\n" + Cores.AZUL + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃          AUDITORIA DE MOVIMENTAÇÕES         ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ Informe o ID da solicitação para auditoria: ");
            Long id = Long.parseLong(sc.nextLine());

            Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);
            if (solicitacao == null) {
                System.out.println(Cores.VERMELHO + "    ⚠ Solicitação #" + id + " não encontrada no sistema." + Cores.RESET);
                return;
            }

            List<HistoricoMovimentacaoSolicitacao> historicos = serviceHistorico.listarPorSolicitacao(id);
            Endereco endereco = serviceEndereco.buscarPorId(solicitacao.getId_endereco());
            System.out.println("\n" + Cores.CIANO + "  [ DADOS DO CHAMADO ]" + Cores.RESET);
            System.out.println("  📜 Título:   " + solicitacao.getTitulo().toUpperCase());
            System.out.println("  📍 Local:    " + endereco.getLogradouro() + ", " + endereco.getNumero() + " - " + endereco.getBairro());
            System.out.println("  📊 Status:   " + solicitacao.getStatus().getStatus());
            System.out.println("  " + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);

            for (int i = 0; i < historicos.size(); i++) {
                HistoricoMovimentacaoSolicitacao historico = historicos.get(i);


                Usuario responsavel = serviceUsuario.buscarPorId(historico.getId_responsavel());

                String data = DataUtil.formatarDataHora(historico.getData_movimentacao());
                String transicao = (historico.getStatus_anterior() != null)
                        ? historico.getStatus_anterior().name() + " ➔ " + historico.getStatus_atual().name()
                        : "Abertura do Chamado (Novo)";

                System.out.println("  " + Cores.AMARELO + "● " + data + Cores.RESET);
                System.out.println("    " + Cores.CIANO + "Op: " + Cores.RESET + transicao);
                System.out.println("    " + Cores.CIANO + "Responsável: " + Cores.RESET + responsavel.getNome() + " (ID: " + responsavel.getId() + ")");

                if (historico.getComentario() != null && !historico.getComentario().isBlank()) {
                    System.out.println("    " + Cores.CIANO + "Justificativa: " + Cores.RESET + "\"" + historico.getComentario() + "\"");
                }


                if (i < historicos.size() - 1) {
                    System.out.println("    " + Cores.AZUL + "│" + Cores.RESET);
                }
            }

            System.out.println("  " + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);
            System.out.println("\n  Pressione ENTER para retornar ao painel de controle...");
            sc.nextLine();

        } catch (NumberFormatException e) {
            System.out.println(Cores.VERMELHO + "    ⚠ ID inválido." + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro na auditoria: " + e.getMessage() + Cores.RESET);
        }
    }

    private void processarListagemMonitoramento(List<Solicitacao> lista, String msgVazia) {
        if (lista.isEmpty()) {
            System.out.println(Cores.AMARELO + "    ℹ " + msgVazia + Cores.RESET);
            return;
        }
        for (Solicitacao s : lista) {
            imprimirCardMonitoramento(s, false);
        }
    }

    private void imprimirCardMonitoramento(Solicitacao solicitacao, boolean isAtrasada) {
        String corDestaque = isAtrasada ? Cores.VERMELHO : Cores.CIANO;

        System.out.println("\n" + corDestaque + "  ID: #" + solicitacao.getId() + " - " + solicitacao.getTitulo().toUpperCase() + Cores.RESET);
        System.out.println("  ┃ Status:    " + solicitacao.getStatus().getStatus());
        System.out.println("  ┃ Prioridade: " + (solicitacao.getPrioridade() != null ? solicitacao.getPrioridade().getPrioridade() : "N/A"));

        if (solicitacao.getDt_prazo() != null) {
            String corPrazo = isAtrasada ? Cores.VERMELHO : Cores.VERDE;
            System.out.println("  ┃ Prazo Final: " + corPrazo + solicitacao.getDt_prazo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (VENCIDO)" + Cores.RESET);
        }

        if (isAtrasada) {
            System.out.println("  ┃ " + Cores.VERMELHO + "⚠ ATENÇÃO: Esta solicitação requer ação imediata!" + Cores.RESET);
        }

        System.out.println("  ┃ Local: " + solicitacao.getDescricao()); // Ou busque o endereço se preferir
        System.out.println("  ┗" + corDestaque + "────────────────────────────────────────────────────────" + Cores.RESET);
    }

    private void imprimirCardComAtendente(Solicitacao solicitacao) {
        System.out.println("\n" + Cores.ROXO + "  ID: #" + solicitacao.getId() + " - " + solicitacao.getTitulo().toUpperCase() + Cores.RESET);
        System.out.println("  ┃ Status:    " + solicitacao.getStatus().getStatus());

        System.out.print("  ┃ Responsável: ");
        if (solicitacao.getId_atendente() == null || solicitacao.getId_atendente() == 0) {
            System.out.println(Cores.VERMELHO + "EQUIPE NÃO VINCULADA" + Cores.RESET);
        } else {
            try {
                Usuario atendente = serviceUsuario.buscarPorId(solicitacao.getId_atendente());
                System.out.println(Cores.VERDE + atendente.getNome() + " (Servidor Público)" + Cores.RESET);
                System.out.println("  ┃   📧 Contato: " + atendente.getEmail());
            } catch (Exception e) {
                System.out.println(Cores.AMARELO + "ID #" + solicitacao.getId_atendente() + " (Erro ao carregar nome)" + Cores.RESET);
            }
        }

        if (solicitacao.getDt_prazo() != null) {
            System.out.println("  ┃ Prazo Final: " + Cores.VERDE + solicitacao.getDt_prazo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + Cores.RESET);
        }

        System.out.println("  ┃ Local: " + solicitacao.getDescricao());
        System.out.println("  ┗" + Cores.ROXO + "────────────────────────────────────────────────────────" + Cores.RESET);
    }
}