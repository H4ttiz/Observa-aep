package br.com.observaacao.dao.categoria;

import br.com.observaacao.dao.DaoUpdate;
import br.com.observaacao.model.categoria.Categoria;

import java.util.List;

public interface DaoCategoria extends DaoUpdate<Categoria> {

    List<Categoria> listarTodosAtivas();
}
