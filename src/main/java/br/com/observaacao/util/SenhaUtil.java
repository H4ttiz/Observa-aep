package br.com.observaacao.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    public static String hash(String senha){
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean verificar(String senhaDigitada, String senhaHash){
        return BCrypt.checkpw(senhaDigitada, senhaHash);
    }
}
