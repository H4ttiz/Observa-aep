package br.com.observaacao.util;

import org.mindrot.jbcrypt.BCrypt;
import java.io.Console;
import java.util.Scanner;

public class SenhaUtil {

    private static final Scanner sc = new Scanner(System.in);

    public static String hash(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean verificar(String senhaDigitada, String senhaHash){
        return BCrypt.checkpw(senhaDigitada, senhaHash);
    }
}