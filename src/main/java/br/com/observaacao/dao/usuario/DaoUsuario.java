package br.com.observaacao.dao.usuario;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.usuario.Usuario;

import java.util.List;

//Operações apenas do Usuario
public interface DaoUsuario extends DaoGenerico<Usuario> {

    Usuario buscarPorEmail(String email);

    Usuario buscarPorCpf(String cpf);

    List<Usuario> buscarTodosVinculados(Long idAdm);

    void ativar(Long id);

    void atualizarSenha(Long id, String senhaHash);

}
