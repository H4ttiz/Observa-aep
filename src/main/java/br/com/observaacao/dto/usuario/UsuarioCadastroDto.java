package br.com.observaacao.dto.usuario;

import br.com.observaacao.model.enums.TipoUsuario;

public record UsuarioCadastroDto(
        String nome,
        String cpf,
        String email,
        String senha,
        TipoUsuario tipoUsuario
) {
}
