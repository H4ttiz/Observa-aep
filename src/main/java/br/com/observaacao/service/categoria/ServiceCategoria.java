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

    public ServiceCategoria(DaoCategoria daoCategoria, DaoUsuario daoUsuario) {
        this.daoCategoria = daoCategoria;
        this.daoUsuario = daoUsuario;
    }

    public Categoria cadastroNormal(Categoria novaCategoria, Long id, Usuario usuario) {

        usuario = daoUsuario.buscarPorId(id);
        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        if(novaCategoria.getCategoria().isEmpty()){
            throw new RuntimeException("Digite a categoria");
        }

        Categoria categoria = new Categoria(
                novaCategoria.getCategoria(),
                novaCategoria.getDescricao()
        );

        daoCategoria.salvar(categoria);

        return categoria;
    }


    public Categoria buscaDeId(Long idCategoria){

        Categoria categoria = daoCategoria.buscarPorId(idCategoria);

        return categoria;
    }


    public List<Categoria> listarTodos(){

        List<Categoria> categorias = daoCategoria.listarTodos();

        return categorias;

    }

    public boolean categoriaExiste(Long id) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        return daoCategoria.buscarPorId(id) != null;
    }

    public Categoria atualizarCategoria(Categoria categoria, Long id, Usuario usuario){
        usuario = daoUsuario.buscarPorId(id);

        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        daoCategoria.atualizar(categoria);

        return categoria;
    }

    public Categoria deletarCategoria(Long id, Usuario usuario){
        usuario = daoUsuario.buscarPorId(id);
        if (ValidacaoUtil.verificarGestor(usuario)){
            throw new RuntimeException("usuario não tem permissão para cadastrar na categoria");
        }

        Categoria categoria = daoCategoria.buscarPorId(id);
        daoCategoria.desativar(id);

        return categoria;
    }



}
