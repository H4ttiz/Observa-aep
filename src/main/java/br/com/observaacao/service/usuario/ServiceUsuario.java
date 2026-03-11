package br.com.observaacao.service.usuario;

import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dto.usuario.UsuarioCadastroDto;
import br.com.observaacao.dto.usuario.UsuarioLoginDto;
import br.com.observaacao.model.usuario.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.util.EmailUtil;
import br.com.observaacao.util.SenhaUtil;



public class ServiceUsuario {
    private final DaoUsuario daoUsuario;

    public ServiceUsuario(DaoUsuario daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public Usuario cadastroNormal(UsuarioCadastroDto dto, TipoUsuario tipoPassado) {

        if (!EmailUtil.emailValido(dto.email())) {
            throw new RuntimeException("Email inválido");
        }

        Usuario existente = daoUsuario.buscarPorEmail(dto.email());

        if (dto.nome().isEmpty()){
            throw new RuntimeException("Nome não pode ser vazio!");
        }

        if (existente != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (dto.senha().length() < 8){
            throw new RuntimeException("A senha deve conter no mínimo 8 caracteres.");
        }

        String senhaHash = SenhaUtil.hash(dto.senha());

        TipoUsuario tipo = tipoPassado == null ? TipoUsuario.C : tipoPassado;

        Usuario usuario = new Usuario(
                dto.nome(),
                dto.email(),
                senhaHash,
                tipo
        );

        daoUsuario.salvar(usuario);
        return usuario;
    }

    public Usuario login(UsuarioLoginDto dto) {

        Usuario usuario = daoUsuario.buscarPorEmail(dto.email());

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        boolean senhaValida = SenhaUtil.verificar(
                dto.senha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new RuntimeException("Senha inválida");
        }

        return usuario;
    }
}
