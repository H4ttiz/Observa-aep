package br.com.observaacao.dao.endereco;

import br.com.observaacao.model.endereco.Endereco;

import java.util.List;

public class DaoEnderecoImpl implements DaoEndereco {

    private static final String TABELA = "enderecos";

    @Override
    public void salvar(Endereco entidade) {

    }

    @Override
    public Endereco buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<Endereco> listarTodos() {
        return List.of();
    }

    @Override
    public void atualizar(Endereco entidade) {

    }

    @Override
    public void desativar(Long id) {

    }


}
