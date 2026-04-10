package br.com.observaacao.util;

import br.com.observaacao.model.enums.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;

public class ValidacaoUtil {

    public static boolean verificaradm(Usuario usuario){

        if (!(usuario.getTipoUsuario() == TipoUsuario.A)){
            return false;
        }
        return true;
    }

}
