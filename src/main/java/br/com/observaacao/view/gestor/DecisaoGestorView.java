package br.com.observaacao.view.gestor;

import br.com.observaacao.model.enums.NivelPrioridade;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;

import java.util.Scanner;

public class DecisaoGestorView {
    private final Scanner sc = new Scanner(System.in);
    private final ServiceSolicitacao serviceSolicitacao;

    public DecisaoGestorView(ServiceSolicitacao serviceSolicitacao) {
        this.serviceSolicitacao = serviceSolicitacao;
    }

    public void aprovar(Usuario gestor) {
        try {
            System.out.println("\n" + Cores.VERDE + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃          APROVAÇÃO DE SOLICITAÇÃO           ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ ID para APROVAÇÃO: ");
            Long id = Long.parseLong(sc.nextLine());

            Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);

            System.out.println("\n  Prioridade: [1] Baixa | [2] Moderada | [3] Média | [4] Alta | [5] Urgente");
            System.out.print("  ▸ Escolha: ");
            int opcaoPrioridade = Integer.parseInt(sc.nextLine());

            NivelPrioridade p = switch (opcaoPrioridade) {
                case 1 -> NivelPrioridade.N1;
                case 2 -> NivelPrioridade.N2;
                case 3 -> NivelPrioridade.N3;
                case 4 -> NivelPrioridade.N4;
                case 5 -> NivelPrioridade.N5;
                default -> NivelPrioridade.N1;
            };

            System.out.print("  ▸ Prazo Estimado (em quantos dias deve ser feito?): ");
            int diasParaSoma = Integer.parseInt(sc.nextLine());
            java.time.LocalDateTime dataPrazo = java.time.LocalDateTime.now().plusDays(diasParaSoma);

            System.out.print("  ▸ Despacho/Comentário do Gestor: ");
            String obs = sc.nextLine();

            solicitacao.setDt_prazo(dataPrazo);
            solicitacao.setStatus(StatusSolicitacao.N3);
            solicitacao.setPrioridade(p);
            solicitacao.setObservacao(obs);

            Loading.executar("Processando aprovação e calculando cronograma");
            serviceSolicitacao.atualizar(id, solicitacao);

            System.out.println(Cores.VERDE + "\n    ✔ Solicitação #" + id + " aprovada com sucesso!" + Cores.RESET);
            System.out.println("    📅 Prazo final definido para: " + dataPrazo.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        } catch (NumberFormatException e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: Digite apenas números para ID, Prioridade e Dias." + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao aprovar: " + e.getMessage() + Cores.RESET);
        }
    }

    public void rejeitar(Usuario gestor) {
        try {
            System.out.println("\n" + Cores.VERMELHO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃          REJEIÇÃO DE SOLICITAÇÃO            ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ ID para REJEIÇÃO: ");
            Long id = Long.parseLong(sc.nextLine());

            Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);

            System.out.print("  ▸ Justificativa Obrigatória (Motivo): ");
            String motivo = sc.nextLine();

            if (motivo.isBlank()) {
                System.out.println(Cores.VERMELHO + "    ⚠ Erro: A rejeição exige um motivo claro para o cidadão!" + Cores.RESET);
                return;
            }

            solicitacao.setStatus(StatusSolicitacao.N6);
            solicitacao.setObservacao(motivo);

            Loading.executar("Registrando indeferimento no sistema");
            serviceSolicitacao.atualizar(id, solicitacao);

            System.out.println(Cores.VERDE + "\n    ✔ Solicitação #" + id + " foi rejeitada com sucesso." + Cores.RESET);
            System.out.println("    📝 Motivo registrado: " + motivo);

        } catch (NumberFormatException e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: O ID deve ser um número válido." + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao processar rejeição: " + e.getMessage() + Cores.RESET);
        }
    }
}