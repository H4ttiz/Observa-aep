package br.com.observaacao.view.adm;

import br.com.observaacao.model.enums.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;

import java.util.List;
import java.util.Scanner;

public class GestaoUsuarioView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceUsuario serviceUsuario;

    public GestaoUsuarioView(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    public void visualizarUsuarios(Usuario adm) {
        System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃           CONTROLE DE USUÁRIOS              ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            List<Usuario> usuarios = serviceUsuario.buscarTodosVinculados(adm.getId());

            if (usuarios.isEmpty()) {
                System.out.println(Cores.AMARELO + "    Nenhum usuário cadastrado no sistema." + Cores.RESET);
            } else {
                System.out.printf(Cores.AZUL + "  %-5s | %-25s | %-10s%n", "ID", "NOME", "PERFIL" + Cores.RESET);
                System.out.println("  " + Cores.CIANO + "─────────────────────────────────────────────" + Cores.RESET);

                for (Usuario usuario : usuarios) {
                    String status = usuario.isAtivo() ? "" : Cores.VERMELHO + " (INATIVO)" + Cores.RESET;
                    System.out.printf("  %-5d | %-25s | %-10s%s%n",
                            usuario.getId(),
                            usuario.getNome().toUpperCase(),
                            usuario.getTipoUsuario().name(),
                            status);
                }
            }
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro ao listar usuários: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }

    public void cadastrarUsuario(Usuario adm) {
        System.out.println("\n" + Cores.VERDE + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          NOVO CADASTRO DE USUÁRIO           ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Nome Completo: ");
            String nome = sc.nextLine();

            System.out.print("  ▸ CPF (apenas números): ");
            String cpf = sc.nextLine();

            System.out.print("  ▸ Email (Login): ");
            String email = sc.nextLine();

            System.out.print("  ▸ Senha Temporária: ");
            String senha = sc.nextLine();

            System.out.println("\n  Selecione o Perfil:");
            System.out.println("  [1] CIDADÃO | [2] ATENDENTE | [3] GESTOR | [4] ADMIN");
            System.out.print("  ▸ Opção: ");
            int opPerfil = Integer.parseInt(sc.nextLine());

            TipoUsuario perfil = converterOpcaoPerfil(opPerfil);

            Usuario novo = new Usuario(adm.getId(), nome, cpf, email, senha, perfil);
            serviceUsuario.cadastroPorAdm(novo,adm.getId());
            System.out.println("\n" + Cores.VERDE + "  ✅ Usuário cadastrado com sucesso!" + Cores.RESET);

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro no cadastro: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("  Pressione ENTER para continuar...");
        sc.nextLine();
    }

    public void desativarUsuario(Usuario adm) {
        System.out.println("\n" + Cores.VERMELHO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃          DESATIVAÇÃO DE USUÁRIO             ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Informe o ID do usuário: ");
            Long id = Long.parseLong(sc.nextLine());

            if (id.equals(adm.getId())) {
                System.out.println(Cores.AMARELO + "    ⚠ Você não pode desativar sua própria conta!" + Cores.RESET);
                return;
            }

            Usuario alvo = serviceUsuario.buscarPorId(id);
            if (alvo == null) {
                System.out.println(Cores.VERMELHO + "    ⚠ Usuário não encontrado." + Cores.RESET);
                return;
            }

            System.out.println("  Tem certeza que deseja desativar " + Cores.AMARELO + alvo.getNome() + Cores.RESET + "?");
            System.out.print("  (S/N): ");
            String confirma = sc.nextLine();

            if (confirma.equalsIgnoreCase("S")) {
                serviceUsuario.desativar(id, adm.getId());
                System.out.println(Cores.VERDE + "  ✅ Usuário desativado com sucesso." + Cores.RESET);
            } else {
                System.out.println("  Ação cancelada.");
            }

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }

    public void ativarUsuario(Usuario adm) {
        System.out.println("\n" + Cores.VERDE + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃           REATIVAÇÃO DE USUÁRIO             ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Informe o ID do usuário para reativar: ");
            String entrada = sc.nextLine();

            if (entrada.isBlank()) return;
            Long id = Long.parseLong(entrada);

            Usuario alvo = serviceUsuario.buscarPorId(id);

            if (alvo == null) {
                System.out.println(Cores.VERMELHO + "    ⚠ Usuário não encontrado." + Cores.RESET);
                return;
            }

            if (alvo.isAtivo()) {
                System.out.println(Cores.AMARELO + "    ⚠ Este usuário já está ATIVO no sistema." + Cores.RESET);
                return;
            }

            System.out.println("  Deseja reativar o acesso de " + Cores.CIANO + alvo.getNome() + Cores.RESET + "?");
            System.out.print("  (S/N): ");
            String confirma = sc.nextLine();

            if (confirma.equalsIgnoreCase("S")) {
                serviceUsuario.ativar(id, adm.getId());
                System.out.println("\n" + Cores.VERDE + "  ✅ Usuário reativado com sucesso! Acesso restabelecido." + Cores.RESET);
            } else {
                System.out.println(Cores.AMARELO + "  Ação cancelada." + Cores.RESET);
            }

        } catch (NumberFormatException e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: O ID deve ser um número válido." + Cores.RESET);
        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Falha ao ativar: " + e.getMessage() + Cores.RESET);
        }

        System.out.println("\n  Pressione ENTER para voltar...");
        sc.nextLine();
    }

    private TipoUsuario converterOpcaoPerfil(int opcao) {
        return switch (opcao) {
            case 1 -> TipoUsuario.C;
            case 2 -> TipoUsuario.S;
            case 3 -> TipoUsuario.G;
            case 4 -> TipoUsuario.A;
            default -> throw new RuntimeException("Opção de perfil inválida.");
        };
    }
}