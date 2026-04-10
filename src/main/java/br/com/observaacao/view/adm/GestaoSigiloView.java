package br.com.observaacao.view.adm;

import br.com.observaacao.model.log.Log;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.log.ServiceLog;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.DataUtil;

import java.util.List;
import java.util.Scanner;

public class GestaoSigiloView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceUsuario serviceUsuario;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceLog serviceLog;

    public GestaoSigiloView(ServiceUsuario serviceUsuario, ServiceLog serviceLog, ServiceSolicitacao serviceSolicitacao) {
        this.serviceUsuario = serviceUsuario;
        this.serviceLog = serviceLog;
        this.serviceSolicitacao = serviceSolicitacao;
    }

    public void visualizarlog(Usuario adm) {
        System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃           AUDITORIA DE SISTEMA (LOGS)       ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            List<Log> logs = serviceLog.listarAuditoria(adm);

            if (logs.isEmpty()) {
                System.out.println(Cores.AMARELO + "    Nenhum registro de log encontrado." + Cores.RESET);
            } else {
                System.out.printf(Cores.AZUL + "  %-18s | %-10s | %-15s%n", "DATA/HORA", "AÇÃO", "TABELA" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "──────────────────────────────────────────────────────────" + Cores.RESET);

                for (Log log : logs) {
                    String data = DataUtil.formatarDataHora(log.getDataExecucao());
                    System.out.printf("  %-18s | %-10s | %-15s%n", data, log.getAcao(), log.getNomeTabela());
                    System.out.println(Cores.RESET + "     ID Usuário: " + log.getIdUsuario() + " | Detalhes: " + log.getDadosAlterados());
                    System.out.println("  " + Cores.CIANO + "╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌" + Cores.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }

    public void visualizarAnonimo(Usuario adm) {
        System.out.println("\n" + Cores.VERMELHO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          QUEBRA DE SIGILO - IDENTIDADE      ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  ⚠ ESTA AÇÃO É MONITORADA E EXIGE SENHA ESPECIAL." + Cores.RESET);

        try {
            System.out.print("\n  ▸ Digite o ID da solicitação anônima: ");
            Long idSolicitacao = Long.parseLong(sc.nextLine());

            Solicitacao s = serviceSolicitacao.buscarPorId(idSolicitacao);

            if (!s.getAnonimo()) {
                System.out.println(Cores.VERDE + "    ℹ Esta solicitação não é anônima." + Cores.RESET);
                return;
            }

            System.out.print(Cores.CIANO + "  ▸ Confirme sua senha de Admin para REVELAR: " + Cores.RESET);
            String senhaDigitada = sc.nextLine();

            if (serviceUsuario.verificarSenha(adm, senhaDigitada)) {
                Usuario solicitante = serviceUsuario.buscarPorId(s.getId_solicitante());

                System.out.println("\n" + Cores.VERDE + "  ✅ IDENTIDADE REVELADA:");
                System.out.println("     NOME: " + solicitante.getNome().toUpperCase());
                System.out.println("     CPF: " + solicitante.getCpf());
                System.out.println("     EMAIL: " + solicitante.getEmail() + Cores.RESET);

                String detalhes = "{\"solicitacao_id\": " + idSolicitacao + ", \"motivo\": \"Visualização de identidade anônima\"}";
                serviceLog.registrarLog(adm.getId(), "solicitacoes_sigilo", "REVELAR", detalhes);

            } else {
                System.out.println(Cores.VERMELHO + "    ⚠ Senha incorreta! Ação abortada e reportada." + Cores.RESET);
                serviceLog.registrarLog(adm.getId(), "seguranca", "SENHA_ERRADA_SIGILO", "{\"id_solicitacao\": " + idSolicitacao + "}");
            }

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }
}