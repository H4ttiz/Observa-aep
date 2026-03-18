package br.com.observaacao.model.endereco;

import br.com.observaacao.model.EntityGenerico;

public class Endereco extends EntityGenerico {

    private Long id;

    public Endereco() {
    }

    @Override
    public Long getId() {
        return id;
    }
}
