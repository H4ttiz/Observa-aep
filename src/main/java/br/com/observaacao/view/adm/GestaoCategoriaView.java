package br.com.observaacao.view.adm;

import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.categoria.ServiceCategoria;
import br.com.observaacao.util.Cores;

import java.util.List;
import java.util.Scanner;

public class GestaoCategoriaView {

    private Scanner sc = new Scanner(System.in);
    private final ServiceCategoria serviceCategoria;

    public GestaoCategoriaView(ServiceCategoria serviceCategoria) {
        this.serviceCategoria = serviceCategoria;
    }

    public void listarCategorias(Usuario adm) {
        System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃           GESTÃO DE CATEGORIAS              ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            List<Categoria> lista = serviceCategoria.listarTodos();

            if (lista.isEmpty()) {
                System.out.println(Cores.AMARELO + "    Nenhuma categoria cadastrada." + Cores.RESET);
            } else {
                System.out.printf(Cores.AZUL + "  %-5s | %-25s | %-10s%n", "ID", "NOME DA CATEGORIA", "STATUS" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "──────────────────────────────────────────────────────────" + Cores.RESET);

                for (Categoria cat : lista) {
                    String statusTexto;
                    if (cat.isAtivo()) {
                        statusTexto = Cores.VERDE + "ATIVA" + Cores.RESET;
                    } else {
                        statusTexto = Cores.VERMELHO + "INATIVA" + Cores.RESET;
                    }

                    System.out.printf("  %-5d | %-25s | %-10s%n",
                            cat.getId(),
                            cat.getCategoria().toUpperCase(),
                            statusTexto
                    );
                }
            }
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao listar: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }

    public void cadastrarCategoria(Usuario adm) {
        System.out.println("\n" + Cores.VERDE + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          NOVA CATEGORIA DE SERVIÇO          ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Nome da Categoria (ex: Iluminação, Asfalto): ");
            String nome = sc.nextLine();

            if (nome.isBlank()) throw new RuntimeException("O nome não pode ser vazio.");

            System.out.print("  ▸ Descrição da Categoria (ex: Iluminação, Asfalto): ");
            String descricao = sc.nextLine();

            if (descricao.isBlank()) throw new RuntimeException("O nome não pode ser vazio.");

            Categoria nova = new Categoria(nome, descricao);

            serviceCategoria.cadastroNormal(nova, adm.getId());
            System.out.println("\n" + Cores.VERDE + "  ✅ Categoria cadastrada com sucesso!" + Cores.RESET);

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: " + e.getMessage() + Cores.RESET);
        }
        sc.nextLine();
    }

    public void editarCategoria(Usuario adm) {
        System.out.println("\n" + Cores.AMARELO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃            EDITAR CATEGORIA                 ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Informe o ID da categoria: ");
            Long id = Long.parseLong(sc.nextLine());

            Categoria existente = serviceCategoria.buscaDeId(id);
            if (existente == null) {
                System.out.println(Cores.VERMELHO + "    ⚠ Categoria não encontrada." + Cores.RESET);
                return;
            }

            System.out.println("  Nome atual: " + Cores.CIANO + existente.getCategoria() + Cores.RESET);
            System.out.print("  ▸ Novo nome (ou ENTER para manter): ");
            String novoNome = sc.nextLine();

            System.out.println("  Descrição atual: " + Cores.CIANO + existente.getCategoria() + Cores.RESET);
            System.out.print("  ▸ Novo Descrição (ou ENTER para manter): ");
            String novoDescricao = sc.nextLine();

            if (!novoNome.isBlank()) {
                existente.setCategoria(novoNome);
            }

            if (!novoDescricao.isBlank()) {
                existente.setCategoria(novoDescricao);
            }

            if (!novoNome.isBlank() || !novoDescricao.isBlank()) {
                serviceCategoria.atualizarCategoria(existente, adm.getId());
            }
            System.out.println(Cores.VERDE + "  ✅ Categoria atualizada!" + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro na edição: " + e.getMessage() + Cores.RESET);
        }
        sc.nextLine();
    }

    public void reativarCategoria(Usuario adm) {
        System.out.println("\n" + Cores.VERDE + "  ▸ [REATIVAR CATEGORIA] Digite o ID: ");
        try {
            System.out.print("\n  ▸ ID da categoria para Reativar: ");
            Long id = Long.parseLong(sc.nextLine());

            System.out.print("  Confirmar Reativação permanente? (S/N): ");
            if (sc.nextLine().equalsIgnoreCase("S")) {
                serviceCategoria.ativarCategoria(id, adm.getId());
                System.out.println(Cores.VERDE + "  ✅ Operação realizada." + Cores.RESET);
            }
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Falha: " + e.getMessage() + Cores.RESET);
        }
        sc.nextLine();
    }

    public void desativarCategoria(Usuario adm) {
        System.out.println("\n" + Cores.VERMELHO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃           EXCLUIR CATEGORIA                 ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  ⚠ Atenção: Categorias vinculadas a solicitações" + Cores.RESET);
        System.out.println(Cores.AMARELO + "  não podem ser excluídas fisicamente." + Cores.RESET);

        try {
            System.out.print("\n  ▸ ID da categoria para excluir: ");
            Long id = Long.parseLong(sc.nextLine());

            System.out.print("  Confirmar exclusão? (S/N): ");
            if (sc.nextLine().equalsIgnoreCase("S")) {
                serviceCategoria.desativarCategoria(id, adm.getId());
                System.out.println(Cores.VERDE + "  ✅ Operação realizada." + Cores.RESET);
            }
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Falha: " + e.getMessage() + Cores.RESET);
        }
        sc.nextLine();
    }
}
