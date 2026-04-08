package br.com.observaacao.view.gestor;

import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;

import java.util.List;

public class MonitoramentoGestorView {
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceUsuario serviceUsuario;

    public MonitoramentoGestorView(ServiceSolicitacao serviceSolicitacao, ServiceUsuario serviceUsuario) {
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceUsuario = serviceUsuario;
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

        for (Solicitacao s : lista) {
            imprimirCardComAtendente(s);
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

        for (Solicitacao s : lista) {
            imprimirCardMonitoramento(s, true);
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