package br.com.observaacao.dao;

import br.com.observaacao.model.EntityGenerico;

import java.util.List;

//Operações Genericas
public interface DaoGenerico<T extends EntityGenerico>{

    void salvar(T entidade);

    T buscarPorId(Long id);

    List<T> listarTodos();

    void atualizar(T entidade);

    void desativar(Long id);
}
