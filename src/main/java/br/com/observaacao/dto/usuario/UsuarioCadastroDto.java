package br.com.observaacao.dto.usuario;

import br.com.observaacao.model.usuario.TipoUsuario;

public record UsuarioCadastroDto(
        String nome,
        String cpf,
        String email,
        String senha,
        TipoUsuario tipoUsuario
) {
}
