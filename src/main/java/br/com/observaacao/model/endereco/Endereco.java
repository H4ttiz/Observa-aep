package br.com.observaacao.model.endereco;

import br.com.observaacao.model.EntityGenerico;

public class Endereco extends EntityGenerico {

    private Long id;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    public Endereco() {
    }

    @Override
    public Long getId() {
        return id;
    }
}
