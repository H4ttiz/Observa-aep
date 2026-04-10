package br.com.observaacao.view.cidadao;

import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.endereco.Endereco;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.historico_movimentacao_solicitacao.ServiceHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.DataUtil;
import br.com.observaacao.util.Loading;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class SolicitacaoCidadaoView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceEndereco serviceEndereco;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceCategoria serviceCategoria;
    private final ServiceHistoricoMovimentacaoSolicitacao serviceHistorico;

    public SolicitacaoCidadaoView(ServiceEndereco serviceEndereco, ServiceSolicitacao serviceSolicitacao,
                                  ServiceCategoria serviceCategoria,  ServiceHistoricoMovimentacaoSolicitacao serviceHistorico) {
        this.serviceEndereco = serviceEndereco;
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceCategoria = serviceCategoria;
        this.serviceHistorico = serviceHistorico;
    }

    public void criarSolicitacao(Usuario usuario) {
        try {
            System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃          ABERTURA DE SOLICITAÇÃO            ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print(Cores.AMARELO + "  ▸ Título: " + Cores.RESET);
            String titulo = sc.nextLine();

            if (titulo.isBlank()) {
                System.out.println(Cores.VERMELHO + "    ⚠ O título é obrigatório!" + Cores.RESET);
                return;
            }

            System.out.print(Cores.AMARELO + "  ▸ Descrição: " + Cores.RESET);
            String descricao = sc.nextLine();

            if (descricao.isBlank()) {
                System.out.println(Cores.VERMELHO + "    ⚠ A descrição é obrigatória!" + Cores.RESET);
                return;
            }

            System.out.print(Cores.AMARELO + "  ▸ Deseja manter anonimato? (S/N): " + Cores.RESET);
            boolean anonimo = sc.nextLine().equalsIgnoreCase("S");

            System.out.println("\n" + Cores.AZUL + "  [ LOCALIZAÇÃO DA OCORRÊNCIA ]" + Cores.RESET);

            System.out.print("    CEP: ");
            String cep = sc.nextLine();
            System.out.print("    Logradouro: ");
            String logradouro = sc.nextLine();
            System.out.print("    Número: ");
            String numero = sc.nextLine();
            System.out.print("    Bairro: ");
            String bairro = sc.nextLine();
            System.out.print("    Cidade: ");
            String cidade = sc.nextLine();
            System.out.print("    Estado (UF): ");
            String estado = sc.nextLine();

            System.out.print("    Deseja adicionar complemento? (S/N): ");
            String complemento = sc.nextLine().equalsIgnoreCase("S") ? lerComplemento() : null;

            Endereco endereco = new Endereco(cep, logradouro, numero, complemento, bairro, cidade, estado);

            Loading.executar("Validando endereço");
            endereco = serviceEndereco.cadastrar(endereco, usuario.getId());
            Long idEndereco = endereco.getId();

            System.out.println("\n" + Cores.AZUL + "  [ SELECIONE UMA CATEGORIA ]" + Cores.RESET);
            List<Categoria> categorias = serviceCategoria.listarTodosAtivas();

            for (Categoria categoria : categorias) {
                System.out.printf("    " + Cores.CIANO + "[%d]" + Cores.RESET + " %-15s | %s\n",
                        categoria.getId(), categoria.getCategoria(), categoria.getDescricao());
            }

            Long idCategoria = 0L;
            while (true) {
                System.out.print(Cores.AMARELO + "\n  ▸ ID da Categoria: " + Cores.RESET);
                try {
                    idCategoria = Long.parseLong(sc.nextLine());
                    if (serviceCategoria.categoriaExiste(idCategoria)) break;
                    System.out.println(Cores.VERMELHO + "    ⚠ Categoria inválida! Escolha um ID da lista." + Cores.RESET);
                } catch (Exception e) {
                    System.out.println(Cores.VERMELHO + "    ⚠ Digite apenas o número do ID." + Cores.RESET);
                }
            }

            Categoria categoriaSelecionada = serviceCategoria.buscaDeId(idCategoria);
            exibirResumo(titulo, descricao, categoriaSelecionada, endereco, anonimo);

            System.out.print(Cores.AMARELO + "  Confirmar envio? (S/N): " + Cores.RESET);
            if (!sc.nextLine().equalsIgnoreCase("S")) {
                System.out.println(Cores.AMARELO + "\n  ✖ Operação cancelada pelo usuário." + Cores.RESET);
                return;
            }

            Loading.executar("Protocolando solicitação");

            Solicitacao solicitacao = new Solicitacao(
                    idCategoria, usuario.getId(), null, idEndereco,
                    StatusSolicitacao.N1, null, anonimo, titulo, descricao,
                    LocalDateTime.now(), null, null,null
            );

            serviceSolicitacao.cadastrar(solicitacao);
            System.out.println("\n" + Cores.VERDE + "  ✔ SOLICITAÇÃO ENVIADA COM SUCESSO!" + Cores.RESET);

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "\n  [!] ERRO CRÍTICO: " + e.getMessage() + Cores.RESET);
        }
    }

    public void visualizarSolicitacao(Usuario usuario) {
        try {
            System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃           MINHAS SOLICITAÇÕES               ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            List<Solicitacao> lista = serviceSolicitacao.buscarPorUsuario(usuario.getId());

            if (lista.isEmpty()) {
                System.out.println(Cores.AMARELO + "    Você ainda não possui solicitações registradas." + Cores.RESET);
                return;
            }

            for (Solicitacao solicitacao : lista) {
                Categoria cat = serviceCategoria.buscaDeId(solicitacao.getId_categoria());

                System.out.println(Cores.AZUL + "  ID: #" + solicitacao.getId() + " - " + solicitacao.getTitulo().toUpperCase() + Cores.RESET);
                System.out.println("  ┃ Status: " + solicitacao.getStatus().getStatus());
                System.out.println("  ┃ Categoria: " + (cat != null ? cat.getCategoria() : "N/A"));
                System.out.println("  ┃ Data: " + solicitacao.getDt_solicitada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                if (solicitacao.getStatus() != StatusSolicitacao.N1) {
                    System.out.println("  ┃ Prioridade: " + (solicitacao.getPrioridade() != null ? solicitacao.getPrioridade().getPrioridade() : "N/A"));

                    if (solicitacao.getDt_prazo() != null) {
                        System.out.println("  ┃ Prazo:      " + Cores.VERDE + solicitacao.getDt_prazo().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + Cores.RESET);
                    }

                    if (solicitacao.getObservacao() != null && !solicitacao.getObservacao().isBlank()) {
                        System.out.println("  ┃ Observação: " + Cores.AMARELO + solicitacao.getObservacao() + Cores.RESET);
                    }
                }

                System.out.println("  ┃ Descrição:  " + solicitacao.getDescricao());
                System.out.println("  ┗" + Cores.CIANO + "────────────────────────────────────────────────────────" + Cores.RESET);
            }

            System.out.println("\n  Pressione ENTER para voltar ao menu...");
            sc.nextLine();

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao carregar visualização: " + e.getMessage() + Cores.RESET);
        }
    }

    public void linhaDoTempoSolicitacao(Usuario usuario) {
        try {
            System.out.println("\n" + Cores.AZUL + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            System.out.println("  ┃        ACOMPANHAR EVOLUÇÃO DO CHAMADO       ┃");
            System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

            System.out.print("  ▸ Digite o ID da solicitação que deseja detalhar: ");
            Long id = Long.parseLong(sc.nextLine());

            Solicitacao solicitacao = serviceSolicitacao.buscarPorId(id);
            if (solicitacao == null || !solicitacao.getId_solicitante().equals(usuario.getId())) {
                System.out.println(Cores.VERMELHO + "    ⚠ Solicitação não encontrada ou acesso negado." + Cores.RESET);
                return;
            }

            List<HistoricoMovimentacaoSolicitacao> historicos = serviceHistorico.listarPorSolicitacao(id);

            System.out.println("\n  📌 " + Cores.CIANO + "RESUMO: " + solicitacao.getTitulo().toUpperCase() + Cores.RESET);
            System.out.println("  " + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);

            for (int i = 0; i < historicos.size(); i++) {
                HistoricoMovimentacaoSolicitacao historico = historicos.get(i);
                String dataFormatada = DataUtil.formatarDataHora(historico.getData_movimentacao());

                boolean ehUltimo = (i == historicos.size() - 1);
                String icone = ehUltimo ? "📍" : "🕒";
                String corStatus = (historico.getStatus_atual() == StatusSolicitacao.N5) ? Cores.VERDE : Cores.AMARELO;

                System.out.println("  " + icone + " " + Cores.CIANO + "[" + dataFormatada + "]" + Cores.RESET);
                System.out.println("     Status: " + corStatus + historico.getStatus_atual().getStatus() + Cores.RESET);

                if (historico.getComentario() != null && !historico.getComentario().isBlank()) {
                    System.out.println("     Nota: " + Cores.RESET + "\"" + historico.getComentario() + "\"" + Cores.RESET);
                }

                if (!ehUltimo) {
                    System.out.println("     " + Cores.CIANO + "┃" + Cores.RESET);
                }
            }

            System.out.println("  " + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);

            if (solicitacao.getStatus() == StatusSolicitacao.N5) {
                System.out.println(Cores.VERDE + "  ✅ Chamado concluído! Obrigado por sua contribuição." + Cores.RESET);
            } else {
                System.out.println(Cores.CIANO + "  ℹ Estamos trabalhando para resolver sua solicitação." + Cores.RESET);
            }

            System.out.println("\n  Pressione ENTER para voltar...");
            sc.nextLine();

        } catch (NumberFormatException e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Digite um ID válido (número)." + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ " + e.getMessage() + Cores.RESET);
        }
    }

    private String lerComplemento() {
        System.out.print("    Complemento: ");
        return sc.nextLine();
    }

    private void exibirResumo(String titulo, String descricao, Categoria categoria, Endereco endereco, boolean anonima) {
        System.out.println("\n" + Cores.CIANO + "  ┏━ REVISÃO DOS DADOS ━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  ┃ Título: " + titulo);
        System.out.println("  ┃ Categoria: " + categoria.getCategoria());
        System.out.println("  ┃ Descrição: " + descricao);
        System.out.println("  ┃ Local: " + endereco.getLogradouro() + ", " + endereco.getNumero() + " - " + endereco.getBairro());
        System.out.println("  ┃ Identidade: " + (anonima ? "Anônima" : "Identificada"));
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + Cores.RESET);
    }
}