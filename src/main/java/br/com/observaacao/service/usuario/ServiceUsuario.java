package br.com.observaacao.service.usuario;

import br.com.observaacao.dao.usuario.DaoUsuario;
import br.com.observaacao.dto.usuario.UsuarioCadastroDto;
import br.com.observaacao.dto.usuario.UsuarioLoginDto;
import br.com.observaacao.model.enums.TipoUsuario;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.log.ServiceLog;
import br.com.observaacao.util.CpfUtil;
import br.com.observaacao.util.EmailUtil;
import br.com.observaacao.util.SenhaUtil;

import java.util.List;

public class ServiceUsuario {
    private final DaoUsuario daoUsuario;
    private final ServiceLog logService;

    public ServiceUsuario(DaoUsuario daoUsuario, ServiceLog logService) {
        this.daoUsuario = daoUsuario;
        this.logService = logService;
    }

    public Usuario cadastroNormal(UsuarioCadastroDto dto, TipoUsuario tipoPassado) {
        validarDadosCadastro(dto.email(), dto.cpf(), dto.nome(), dto.senha());

        Usuario cpfJaCadastrado = daoUsuario.buscarPorCpf(dto.cpf().replaceAll("\\D", ""));
        Usuario emailJaCadastrado = daoUsuario.buscarPorEmail(dto.email());

        if (cpfJaCadastrado != null) throw new RuntimeException("CPF já cadastrado");
        if (emailJaCadastrado != null) throw new RuntimeException("Email já cadastrado");

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

        String detalhes = String.format("{\"email\": \"%s\", \"tipo\": \"%s\"}", usuario.getEmail(), tipo);
        logService.registrarLog(0L, "usuarios", "SELF_REGISTER", detalhes); // 0L pois ele ainda não tinha ID

        return usuario;
    }

    public Usuario login(UsuarioLoginDto dto) {
        Usuario usuario = daoUsuario.buscarPorEmail(dto.email());

        if (usuario == null) {
            logService.registrarLog(0L, "usuarios", "LOGIN_FAIL", "{\"email_tentativa\": \"" + dto.email() + "\"}");
            throw new RuntimeException("Usuário não encontrado");
        }

        boolean senhaValida = SenhaUtil.verificar(dto.senha(), usuario.getSenha());

        if (!senhaValida) {
            logService.registrarLog(usuario.getId(), "usuarios", "LOGIN_FAIL", "{\"motivo\": \"senha_incorreta\"}");
            throw new RuntimeException("Senha inválida");
        }

        logService.registrarLog(usuario.getId(), "usuarios", "LOGIN_SUCCESS", "{}");
        return usuario;
    }

    public void cadastroPorAdm(Usuario usuario, Long idAdmExecutor) {
        validarDadosCadastro(usuario.getEmail(), usuario.getCpf(), usuario.getNome(), usuario.getSenha());

        if (daoUsuario.buscarPorCpf(usuario.getCpf().replaceAll("\\D", "")) != null) throw new RuntimeException("CPF já cadastrado");
        if (daoUsuario.buscarPorEmail(usuario.getEmail()) != null) throw new RuntimeException("Email já cadastrado");

        String senhaHash = SenhaUtil.hash(usuario.getSenha());
        TipoUsuario tipo = usuario.getTipoUsuario() == null ? TipoUsuario.C : usuario.getTipoUsuario();

        Usuario usuarionovo = new Usuario(
                idAdmExecutor,
                usuario.getNome(),
                usuario.getCpf().replaceAll("\\D", ""),
                usuario.getEmail(),
                senhaHash,
                tipo
        );

        daoUsuario.salvar(usuarionovo);

        String detalhes = String.format("{\"novo_usuario_email\": \"%s\", \"perfil\": \"%s\"}", usuarionovo.getEmail(), tipo);
        logService.registrarLog(idAdmExecutor, "usuarios", "ADMIN_CREATE_USER", detalhes);
    }

    public void desativar(Long id, Long idAdmExecutor) { // Adicionado idAdmExecutor
        if (id == null || id <= 0) throw new RuntimeException("Id do usuario inválido");
        if (id.equals(idAdmExecutor)) throw new RuntimeException("Não é possível desativar a própria conta");

        Usuario alvo = daoUsuario.buscarPorId(id);
        if (alvo == null) throw new RuntimeException("Usuário não encontrado");

        daoUsuario.desativar(id);

        String detalhes = String.format("{\"usuario_desativado_id\": %d, \"nome\": \"%s\"}", id, alvo.getNome());
        logService.registrarLog(idAdmExecutor, "usuarios", "DISABLE", detalhes);
    }

    public void ativar(Long id, Long idAdmExecutor) {
        if (id == null || id <= 0) throw new RuntimeException("Id do usuario inválido");

        Usuario alvo = daoUsuario.buscarPorId(id);
        if (alvo == null) throw new RuntimeException("Usuário não encontrado");

        daoUsuario.ativar(id);

        String detalhes = String.format("{\"usuario_ativado_id\": %d, \"nome\": \"%s\"}", id, alvo.getNome());
        logService.registrarLog(idAdmExecutor, "usuarios", "ENABLE", detalhes);
    }


    public Usuario buscarPorId(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("ID de usuário inválido.");
        Usuario usuario = daoUsuario.buscarPorId(id);
        if (usuario == null) throw new RuntimeException("Usuário não encontrado.");
        return usuario;
    }

    public List<Usuario> buscarTodosVinculados(Long idAdm) {
        if (idAdm == null || idAdm <= 0) throw new IllegalArgumentException("ID do Administrador inválido.");
        return daoUsuario.buscarTodosVinculados(idAdm);
    }

    public boolean verificarSenha(Usuario adm, String senhaDigitada) {
        try {
            Usuario usuarioBanco = buscarPorId(adm.getId());
            return SenhaUtil.verificar(senhaDigitada, usuarioBanco.getSenha());
        } catch (Exception e) {
            return false;
        }
    }

    public void atualizarSenha(Long id, String novaSenha) {
        Usuario alvo = buscarPorId(id);

        if (novaSenha == null || novaSenha.length() < 8) {
            throw new RuntimeException("A nova senha deve conter no mínimo 8 caracteres.");
        }

        String senhaHash = SenhaUtil.hash(novaSenha);

        daoUsuario.atualizarSenha(id, senhaHash);

        String detalhes = String.format(
                "{\"usuario_id\": %d, \"nome\": \"%s\", \"evento\": \"Senha alterada pelo próprio usuário\"}",
                id, alvo.getNome()
        );

        logService.registrarLog(id, "usuarios", "PASSWORD_CHANGE", detalhes);
    }

    private void validarDadosCadastro(String email, String cpf, String nome, String senha) {
        if (!EmailUtil.emailValido(email)) throw new RuntimeException("Email inválido");
        if (!CpfUtil.validaCpf(cpf.replaceAll("\\D", ""))) throw new RuntimeException("CPF inválido");
        if (nome == null || nome.isEmpty()) throw new RuntimeException("Nome não pode ser vazio!");
        if (senha == null || senha.length() < 8) throw new RuntimeException("A senha deve conter no mínimo 8 caracteres.");
    }
}