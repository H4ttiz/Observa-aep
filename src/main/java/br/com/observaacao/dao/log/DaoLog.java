package br.com.observaacao.dao.log;

import br.com.observaacao.model.log.Log;

import java.util.List;

public interface DaoLog {

    void salvar(Log log);

    List<Log> listarTodos();
}
