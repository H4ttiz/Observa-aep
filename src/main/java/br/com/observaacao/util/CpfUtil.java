package br.com.observaacao.util;

public class CpfUtil {

    public static boolean validaCpf(String cpf) {

        if (cpf == null || cpf.length() != 11) return false;

        if (cpf.chars().distinct().count() == 1) return false;

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }

        int digito1 = 11 - (soma % 11);
        if (digito1 >= 10) digito1 = 0;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }

        int digito2 = 11 - (soma % 11);
        if (digito2 >= 10) digito2 = 0;


        return digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');
    }

    public String formatarCpf(String cpf) {

        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return cpf.substring(0, 3) + "." +
                cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" +
                cpf.substring(9, 11);
    }
}
