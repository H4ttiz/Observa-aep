package br.com.observaacao.view.gestor;

import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.CpfUtil;
import br.com.observaacao.util.Loading;

import java.util.List;

public class SolicitacaoGestorView {

    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceUsuario serviceUsuario;
    private final ServiceEndereco serviceEndereco;
    private final CpfUtil cpfUtil = new CpfUtil();

    public SolicitacaoGestorView(ServiceSolicitacao serviceSolicitacao, ServiceUsuario serviceUsuario, ServiceEndereco serviceEndereco) {
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceUsuario = serviceUsuario;
        this.serviceEndereco = serviceEndereco;
    }

    public void exibirTodas() {
        System.out.println("\n" + Cores.AZUL + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃        RELATÓRIO GERAL DE SOLICITAÇÕES      ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        Loading.executar("Buscando registros no banco");
        List<Solicitacao> lista = serviceSolicitacao.listarTodos();

        if (lista.isEmpty()) {
            System.out.println(Cores.AMARELO + "    ⚠ Nenhuma solicitação encontrada no sistema." + Cores.RESET);
            return;
        }

        for (Solicitacao s : lista) {
            imprimirCard(s);
        }
    }

    public void exibirPendentes() {
        System.out.println("\n" + Cores.AMARELO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃       FILA: AGUARDANDO APROVAÇÃO (N1)       ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        Loading.executar("Buscando novas solicitações");
        List<Solicitacao> lista = serviceSolicitacao.buscarSolicitacaoPendente();

        if (lista.isEmpty()) {
            System.out.println(Cores.VERDE + "    ✔ Não há solicitações aguardando aprovação." + Cores.RESET);
            return;
        }

        for (Solicitacao s : lista) {
            imprimirCard(s);
        }
    }

    private void imprimirCard(Solicitacao s) {
        String corStatus = switch (s.getStatus()) {
            case N1 -> Cores.AMARELO;
            case N2 -> Cores.AZUL;
            case N3 -> Cores.CIANO;
            case N4 -> Cores.ROXO;
            case N5 -> Cores.VERDE;
            case N6 -> Cores.VERMELHO;
            default -> Cores.RESET;
        };

        System.out.println("\n" + Cores.CIANO + "  ID: #" + s.getId() + " - " + s.getTitulo().toUpperCase() + Cores.RESET);
        System.out.println("  ┃ Status: " + corStatus + s.getStatus().getStatus() + Cores.RESET);
        System.out.println("  ┃ Data:   " + s.getDt_solicitada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        System.out.print("  ┃ Solicitante: ");
        if (s.getAnonimo()) {
            System.out.println(Cores.AMARELO + "ANÔNIMO" + Cores.RESET);
        } else {
            try {
                Usuario u = serviceUsuario.buscarPorId(s.getId_solicitante());
                System.out.println(Cores.VERDE + u.getNome() + Cores.RESET);
                System.out.println("  ┃   📧 Email: " + u.getEmail());
                System.out.println("  ┃   🆔 CPF:   " + cpfUtil.formatarCpf(u.getCpf()));
            } catch (Exception e) {
                System.out.println(Cores.VERMELHO + "Erro ao carregar dados do usuário" + Cores.RESET);
            }
        }

        System.out.println("  ┃ Localização:");
        try {
            var end = serviceEndereco.buscarPorId(s.getId_endereco());
            System.out.println("  ┃   📍 " + end.getLogradouro() + ", " + end.getNumero() + " - " + end.getBairro());
            System.out.println("  ┃   🏙️ " + end.getCidade() + " / " + end.getEstado());
        } catch (Exception e) {
            System.out.println("  ┃   ⚠ Endereço não localizado.");
        }

        System.out.println("  ┃ Descrição: " + s.getDescricao());
        System.out.println("  ┗" + Cores.CIANO + "────────────────────────────────────────────────────────" + Cores.RESET);
    }
}