package br.com.observaacao.util;

import br.com.observaacao.model.usuario.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;

public class ValidacaoUtil {

    public static boolean verificarGestor(Usuario usuario){

        if (!(usuario.getTipoUsuario() == TipoUsuario.G)){
            return false;
        }
        return true;
    }

}
