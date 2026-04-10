package br.com.observaacao.view;

import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.usuario.ServiceUsuario;
import br.com.observaacao.util.Cores;

import java.util.Scanner;

public class AlterarSenhaView {

    private final Scanner sc = new Scanner(System.in);
    private final ServiceUsuario serviceUsuario;

    public AlterarSenhaView(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    public void exibirTela(Usuario usuarioLogado) {
        System.out.println("\n" + Cores.CIANO + "  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("  ┃            SEGURANÇA DA CONTA               ┃");
        System.out.println("  ┃              ALTERAR SENHA                  ┃");
        System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛" + Cores.RESET);

        try {
            System.out.print("  ▸ Digite a senha atual: ");
            String senhaAtual = sc.nextLine();

            if (!serviceUsuario.verificarSenha(usuarioLogado, senhaAtual)) {
                System.out.println(Cores.VERMELHO + "    ⚠ Senha atual incorreta. Ação cancelada." + Cores.RESET);
                return;
            }

            System.out.print("  ▸ Digite a NOVA senha (mín. 8 caracteres): ");
            String novaSenha = sc.nextLine();

            System.out.print("  ▸ Confirme a nova senha: ");
            String confirmacao = sc.nextLine();

            if (!novaSenha.equals(confirmacao)) {
                System.out.println(Cores.VERMELHO + "    ⚠ As senhas não coincidem!" + Cores.RESET);
                return;
            }

            serviceUsuario.atualizarSenha(usuarioLogado.getId(), novaSenha);

            System.out.println("\n" + Cores.VERDE + "  ✅ Senha alterada com sucesso!" + Cores.RESET);

        } catch (Exception e) {
            System.out.println(Cores.VERMELHO + "    ⚠ Erro: " + e.getMessage() + Cores.RESET);
        }
    }
}
