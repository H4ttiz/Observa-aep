package br.com.observaacao.dao;

import br.com.observaacao.model.EntityGenerico;

public interface DaoUpdate<T extends EntityGenerico> extends DaoBase<T>{

    void ativar(Long id);

    void desativar(Long id);

    void atualizar(T entidade);
}
