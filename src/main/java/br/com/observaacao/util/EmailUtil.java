package br.com.observaacao.util;

import java.util.regex.Pattern;

public class EmailUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean emailValido(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}