package br.com.observaacao.view.gestor;

import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;

public class MonitoramentoGestorView {
    private final ServiceSolicitacao service;

    public MonitoramentoGestorView(ServiceSolicitacao service) {
        this.service = service;
    }

    public void verAndamento() {
        System.out.println("\n" + Cores.CIANO + "  [ SOLICITAÇÕES EM EXECUÇÃO ]" + Cores.RESET);
        Loading.executar("Mapeando equipes de rua");
    }

    public void verAtrasadas() {
        System.out.println("\n" + Cores.VERMELHO + "  ⚠️ [ ALERTA CRÍTICO: PRAZOS VENCIDOS ]" + Cores.RESET);
        Loading.executar("Analisando cronogramas");
    }
}