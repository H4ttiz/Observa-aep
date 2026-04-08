package br.com.observaacao.service.usuario;

import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dto.usuario.UsuarioCadastroDto;
import br.com.observaacao.dto.usuario.UsuarioLoginDto;
import br.com.observaacao.model.enums.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.util.CpfUtil;
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

        if (!CpfUtil.validaCpf(dto.cpf().replaceAll("\\D", ""))) {
            throw new RuntimeException("CPF inválido");
        }

        Usuario cpfJaCadastrado =  daoUsuario.buscarPorCpf(dto.cpf().replaceAll("\\D", ""));

        Usuario emailJaCadastrado = daoUsuario.buscarPorEmail(dto.email());

        if (dto.nome().isEmpty()){
            throw new RuntimeException("Nome não pode ser vazio!");
        }

        if (cpfJaCadastrado != null) {
            throw new RuntimeException("CPF já cadastrado");
        }

        if (emailJaCadastrado != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (dto.senha().length() < 8){
            throw new RuntimeException("A senha deve conter no mínimo 8 caracteres.");
        }

        String senhaHash = SenhaUtil.hash(dto.senha());

        TipoUsuario tipo = tipoPassado == null ? TipoUsuario.C : tipoPassado;

        Usuario usuario = new Usuario(
                null,
                dto.nome(),
                dto.cpf().replaceAll("\\D", ""),
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
