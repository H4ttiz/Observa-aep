package br.com.observaacao.view.cidadao;

import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.endereco.Endereco;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.service.endereco.ServiceEndereco;
import br.com.observaacao.service.solicitacao.ServiceSolicitacao;
import br.com.observaacao.util.Cores;
import br.com.observaacao.util.Loading;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class SolicitacaoView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceEndereco serviceEndereco;
    private final ServiceSolicitacao serviceSolicitacao;
    private final ServiceCategoria serviceCategoria;

    public SolicitacaoView(ServiceEndereco serviceEndereco, ServiceSolicitacao serviceSolicitacao, ServiceCategoria serviceCategoria) {
        this.serviceEndereco = serviceEndereco;
        this.serviceSolicitacao = serviceSolicitacao;
        this.serviceCategoria = serviceCategoria;
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
            endereco = serviceEndereco.cadastrar(endereco);
            Long idEndereco = endereco.getId();

            System.out.println("\n" + Cores.AZUL + "  [ SELECIONE UMA CATEGORIA ]" + Cores.RESET);
            List<Categoria> categorias = serviceCategoria.listarTodos();

            for (Categoria c : categorias) {
                System.out.printf("    " + Cores.CIANO + "[%d]" + Cores.RESET + " %-15s | %s\n",
                        c.getId(), c.getCategoria(), c.getDescricao());
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
                    LocalDateTime.now(), null, null
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

            for (Solicitacao s : lista) {
                Categoria cat = serviceCategoria.buscaDeId(s.getId_categoria());

                System.out.println(Cores.AZUL + "  ID: #" + s.getId() + " - " + s.getTitulo().toUpperCase() + Cores.RESET);
                System.out.println("  ┃ Status: " + formatarStatus(s.getStatus()));
                System.out.println("  ┃ Categoria: " + (cat != null ? cat.getCategoria() : "N/A"));
                System.out.println("  ┃ Data: " + s.getDt_solicitada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.println("  ┃ Descrição: " + s.getDescricao());
                System.out.println("  ┗" + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);
            }

            System.out.println("\n  Pressione ENTER para voltar ao menu...");
            sc.nextLine();

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao carregar visualização: " + e.getMessage() + Cores.RESET);
        }
    }


    private String formatarStatus(StatusSolicitacao status) {
        return switch (status) {
            case N1 -> Cores.AMARELO + "AGUARDANDO" + Cores.RESET;
            case N2 -> Cores.CIANO + "EM ANÁLISE" + Cores.RESET;
            case N3 -> Cores.VERDE + "CONCLUÍDO" + Cores.RESET;
            default -> status.name();
        };
    }

    private String lerComplemento() {
        System.out.print("    Complemento: ");
        return sc.nextLine();
    }

    private void exibirResumo(String tit, String desc, Categoria cat, Endereco end, boolean anon) {
        System.out.println("\n" + Cores.CIANO + "  ┏━ REVISÃO DOS DADOS ━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  ┃ Título: " + tit);
        System.out.println("  ┃ Categoria: " + cat.getCategoria());
        System.out.println("  ┃ Local: " + end.getLogradouro() + ", " + end.getNumero() + " - " + end.getBairro());
        System.out.println("  ┃ Identidade: " + (anon ? "Anônima" : "Identificada"));
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + Cores.RESET);
    }
}