package br.com.observaacao.dao.categoria;

import br.com.observaacao.dao.DaoGenerico;
import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.usuario.Usuario;

import java.util.List;

public interface DaoCategoria extends DaoGenerico<Categoria> {
    void ativar(Long id);
    List<Categoria> listarTodosAtivas();
}
