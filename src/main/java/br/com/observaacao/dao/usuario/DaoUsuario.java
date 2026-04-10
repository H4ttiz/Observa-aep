package br.com.observaacao.dao.usuario;

import br.com.observaacao.dao.DaoUpdate;
import br.com.observaacao.model.usuario.Usuario;

import java.util.List;

//Operações apenas do Usuario
public interface DaoUsuario extends DaoUpdate<Usuario> {

    Usuario buscarPorEmail(String email);

    Usuario buscarPorCpf(String cpf);

    List<Usuario> buscarTodosVinculados(Long idAdm);

    void atualizarSenha(Long id, String senhaHash);

}
