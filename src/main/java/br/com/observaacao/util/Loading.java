package br.com.observaacao.util;

public class Loading {

    public static void executar(String mensagem) {
        String[] animacao = {"|", "/", "-", "\\"};

        System.out.print(mensagem + " ");

        for (int i = 0; i < 10; i++) {
            System.out.print("\r" + mensagem + " " + animacao[i % animacao.length]);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.print("\r" + mensagem + " ✔\n");
    }
}
