package br.com.observaacao.dto.usuario;

import br.com.observaacao.model.usuario.TipoUsuario;

public record UsuarioCadastroDto(
        String nome,
        String email,
        String senha,
        TipoUsuario tipoUsuario
) {
}
