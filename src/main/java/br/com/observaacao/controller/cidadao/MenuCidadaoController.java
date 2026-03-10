package br.com.observaacao.controller.cidadao;

import br.com.observaacao.model.usuario.Usuario;

import java.util.Scanner;

public class MenuCidadaoController {
    private final Scanner sc = new Scanner(System.in);

    public void menu(Usuario usuario) {

        while (true) {

            System.out.println("\n=== MENU CIDADÃO ===");
            System.out.println("Bem vindo " + usuario.getNome());

            System.out.println("1 - Criar solicitação");
            System.out.println("2 - Ver minhas solicitações");
            System.out.println("0 - Logout");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {

                case 1 -> System.out.println("Abrir solicitação (em construção)");

                case 2 -> System.out.println("Listar solicitações (em construção)");

                case 0 -> {
                    System.out.println("Logout realizado.");
                    return;
                }

                default -> System.out.println("Opção inválida");
            }
        }
    }
}
