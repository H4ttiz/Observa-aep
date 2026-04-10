package br.com.observaacao.view.atendente;

import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.DataUtil;
import br.com.observaacao.util.Loading;

import java.time.LocalDateTime;
import java.util.Scanner;

public class AtendimentoView {
    private final Scanner sc = new Scanner(System.in);

    private final ServiceSolicitacao serviceSolicitacao;

    public AtendimentoView(ServiceSolicitacao serviceSolicitacao) {
        this.serviceSolicitacao = serviceSolicitacao;
    }

    public void puxarSolicitacao(Usuario atendente) {
        System.out.println("\n" + Cores.AMARELO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          VINCULAR EQUIPE TÉCNICA            ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        System.out.print("  ▸ ID da solicitação para assumir: ");
        Long id = Long.parseLong(sc.nextLine());

        Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);
        solicitacao.setId_atendente(atendente.getId());
        solicitacao.setStatus(StatusSolicitacao.N4);

        serviceSolicitacao.atualizar(id, solicitacao, atendente);

        Loading.executar("Vinculando sua conta à solicitação #" + id);
        System.out.println(Cores.VERDE + "  ✔ Sucesso! Você agora é o responsável por esta demanda." + Cores.RESET);
    }

        public void atualizarAndamento(Usuario atendente) {
            System.out.println("\n" + Cores.ROXO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃           DIÁRIO DE ATUALIZAÇÃO             ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ ID da solicitação: ");
            Long id = Long.parseLong(sc.nextLine());

            System.out.print("  ▸ Relate o progresso/observação: ");
            String novaObservacao = sc.nextLine();

            Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);
            solicitacao.setObservacao(solicitacao.getObservacao() + "\n- " + novaObservacao + " - " + DataUtil.formatarDataHora(LocalDateTime.now()));

            serviceSolicitacao.atualizar(id, solicitacao, atendente);

            Loading.executar("Salvando atualização no diário de bordo");
            System.out.println(Cores.VERDE + "  ✔ Observação registrada com sucesso!" + Cores.RESET);
        }

        public void finalizarSolicitacao(Usuario atendente) {
            System.out.println("\n" + Cores.VERDE + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃          FINALIZAR ATENDIMENTO              ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ ID da solicitação concluída: ");
            Long id = Long.parseLong(sc.nextLine());

            System.out.print("  ▸ Parecer Final do Atendimento: ");
            String parecer = sc.nextLine();

            Solicitacao  solicitacao = serviceSolicitacao.buscarPorId(id);
            solicitacao.setStatus(StatusSolicitacao.N5);
            solicitacao.setDt_finalizada(LocalDateTime.now());
            solicitacao.setObservacao(solicitacao.getObservacao() + "\nFINALIZADO: " + parecer + " - " + DataUtil.formatarDataHora(LocalDateTime.now()));
            serviceSolicitacao.atualizar(id, solicitacao, atendente);

            Loading.executar("Encerrando chamado e gerando protocolo de conclusão");
            System.out.println(Cores.VERDE + "  ✔ Solicitação #" + id + " marcada como FINALIZADA!" + Cores.RESET);
        }
    }
