package br.com.observaacao.service.categoria;

import br.com.observaacao.dao.categoria.DaoCategoria;

import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.util.ValidacaoUtil;

import java.util.List;


public class ServiceCategoria {

    private final DaoCategoria daoCategoria;
    private final DaoUsuario daoUsuario;
    private Usuario usuario;


    public ServiceCategoria(DaoCategoria daoCategoria, DaoUsuario daoUsuario, Usuario usuario) {
        this.daoCategoria = daoCategoria;
        this.daoUsuario = daoUsuario;
        this.usuario = usuario;
    }

    public Categoria cadastroNormal(Categoria novaCategoria, Long id) {

        usuario = daoUsuario.buscarPorId(id);
        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        if(novaCategoria.getCategoria().isEmpty()){
            throw new RuntimeException("Digite a categoria");
        }

        Categoria categoria = new Categoria(
                novaCategoria.getCategoria(),
                novaCategoria.getDescricao(),
                novaCategoria.getNivelPrioridade()
        );

        daoCategoria.salvar(categoria);

        return categoria;
    }


    public Categoria buscaDeId(Long idCategoria, Long idUsuario){
        usuario = daoUsuario.buscarPorId(idUsuario);

        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        Categoria categoria = daoCategoria.buscarPorId(idCategoria);

        return categoria;
    }


    public List<Categoria> listarTodos(Long id){
        usuario = daoUsuario.buscarPorId(id);

        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        List<Categoria> categorias = daoCategoria.listarTodos();

        return categorias;

    }


    public Categoria atualizarCategoria(Categoria categoria, Long id){
        usuario = daoUsuario.buscarPorId(id);

        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        daoCategoria.atualizar(categoria);

        return categoria;
    }

    public Categoria deletarCategoria(Long id){
        usuario = daoUsuario.buscarPorId(id);
        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        Categoria categoria = daoCategoria.buscarPorId(id);
        daoCategoria.desativar(id);

        return categoria;
    }



}
