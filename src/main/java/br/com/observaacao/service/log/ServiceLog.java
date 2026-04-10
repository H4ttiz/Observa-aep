package br.com.observaacao.service.log;

import br.com.observaacao.dao.log.DaoLog;
import br.com.observaacao.model.log.Log;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.util.ValidacaoUtil;

import java.util.List;

public class ServiceLog {

    private final DaoLog daoLog;

    public ServiceLog(DaoLog daoLog) {
        this.daoLog = daoLog;
    }

    public void registrarLog(Long idUsuario, String tabela, String acao, String detalhesJson) {
        Log log = new Log(idUsuario, tabela, acao, detalhesJson);
        daoLog.salvar(log);
    }

    public List<Log> listarAuditoria(Usuario usuarioLogado) {
        if (!ValidacaoUtil.verificaradm(usuarioLogado)) {
            throw new RuntimeException("Acesso Negado: Apenas administradores podem visualizar os logs de auditoria.");
        }
        return daoLog.listarTodos();
    }
}
