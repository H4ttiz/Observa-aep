package br.com.observaacao.dao.endereco;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.endereco.Endereco;
import br.com.observaacao.model.usuario.Usuario;

public interface DaoEndereco extends DaoGenerico<Endereco> {

    Endereco buscarPorCep(String cep);
}
