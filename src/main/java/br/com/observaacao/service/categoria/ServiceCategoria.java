package br.com.observaacao.service.categoria;

import br.com.observaacao.dao.categoria.DaoCategoria;
import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.log.ServiceLog;
import br.com.observaacao.util.ValidacaoUtil;

import java.util.List;

public class ServiceCategoria {

    private final DaoCategoria daoCategoria;
    private final DaoUsuario daoUsuario;
    private final ServiceLog logService;

    public ServiceCategoria(DaoCategoria daoCategoria, DaoUsuario daoUsuario, ServiceLog logService) {
        this.daoCategoria = daoCategoria;
        this.daoUsuario = daoUsuario;
        this.logService = logService;
    }

    public Categoria cadastroNormal(Categoria novaCategoria, Long idUsuarioAdm) {
        Usuario usuario = daoUsuario.buscarPorId(idUsuarioAdm);

        if (!ValidacaoUtil.verificaradm(usuario)) {
            throw new RuntimeException("Usuário não tem permissão para cadastrar categoria.");
        }

        if (novaCategoria.getCategoria().isEmpty()) {
            throw new RuntimeException("Digite o nome da categoria.");
        }

        Categoria categoria = new Categoria(
                novaCategoria.getCategoria(),
                novaCategoria.getDescricao()
        );

        daoCategoria.salvar(categoria);

        String detalhes = "{\"nome\": \"" + categoria.getCategoria() + "\", \"acao\": \"Criação de nova categoria\"}";
        logService.registrarLog(idUsuarioAdm, "categorias", "INSERT", detalhes);

        return categoria;
    }

    public Categoria buscaDeId(Long idCategoria) {
        return daoCategoria.buscarPorId(idCategoria);
    }

    public List<Categoria> listarTodos() {
        return daoCategoria.listarTodos();
    }

    public List<Categoria> listarTodosAtivas() {
        return daoCategoria.listarTodosAtivas();
    }

    public boolean categoriaExiste(Long id) {
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }
        return daoCategoria.buscarPorId(id) != null;
    }

    public Categoria atualizarCategoria(Categoria categoria, Long idUsuarioAdm) {
        Usuario usuario = daoUsuario.buscarPorId(idUsuarioAdm);

        if (!ValidacaoUtil.verificaradm(usuario)) {
            throw new RuntimeException("Usuário não tem permissão para atualizar categoria.");
        }

        daoCategoria.atualizar(categoria);

        String detalhes = "{\"id_categoria\": " + categoria.getId() + ", \"novo_nome\": \"" + categoria.getCategoria() + "\"}";
        logService.registrarLog(idUsuarioAdm, "categorias", "UPDATE", detalhes);

        return categoria;
    }

    public void desativarCategoria(Long id, Long idUsuarioAdm) {
        Usuario usuario = daoUsuario.buscarPorId(idUsuarioAdm);
        if (!ValidacaoUtil.verificaradm(usuario)) {
            throw new RuntimeException("Usuário não tem permissão para desativar categoria.");
        }

        daoCategoria.desativar(id);

        String detalhes = "{\"id_categoria\": " + id + ", \"status\": \"inativo\"}";
        logService.registrarLog(idUsuarioAdm, "categorias", "DISABLE", detalhes);
    }

    public void ativarCategoria(Long id, Long idUsuarioAdm) {
        Usuario usuario = daoUsuario.buscarPorId(idUsuarioAdm);
        if (!ValidacaoUtil.verificaradm(usuario)) {
            throw new RuntimeException("Usuário não tem permissão para ativar categoria.");
        }

        daoCategoria.ativar(id);

        String detalhes = "{\"id_categoria\": " + id + ", \"status\": \"ativo\"}";
        logService.registrarLog(idUsuarioAdm, "categorias", "ENABLE", detalhes);
    }
}