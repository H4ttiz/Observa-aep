package br.com.observaacao.dao;

import br.com.observaacao.model.EntityGenerico;

import java.util.List;

//Operações Genericas
public interface DaoBase<T extends EntityGenerico>{

    Long salvar(T entidade);

    T buscarPorId(Long id);

    List<T> listarTodos();

}
