package br.com.observaacao.dao.usuario;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.usuario.Usuario;

//Operações apenas do Usuario
public interface DaoUsuario extends DaoGenerico<Usuario> {

    Usuario buscarPorEmail(String email);

    Usuario buscarPorCpf(String cpf);
}
