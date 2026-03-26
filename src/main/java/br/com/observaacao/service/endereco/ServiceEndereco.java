package br.com.observaacao.service.endereco;

import br.com.observaacao.dao.endereco.DaoEndereco;
import br.com.observaacao.model.endereco.Endereco;

import java.util.List;

public class ServiceEndereco {

    private final DaoEndereco daoEndereco;

    public ServiceEndereco(DaoEndereco daoEndereco) {
        this.daoEndereco = daoEndereco;
    }

    public Endereco cadastrar(Endereco endereco) {

        if (endereco.getCep() == null || endereco.getCep().isBlank()) {
            throw new RuntimeException("CEP não pode ser vazio");
        }

        if (endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()) {
            throw new RuntimeException("Logradouro não pode ser vazio");
        }

        if (endereco.getNumero() == null || endereco.getNumero().isBlank()) {
            throw new RuntimeException("Número não pode ser vazio");
        }

        if (endereco.getBairro() == null || endereco.getBairro().isBlank()) {
            throw new RuntimeException("Bairro não pode ser vazio");
        }

        if (endereco.getCidade() == null || endereco.getCidade().isBlank()) {
            throw new RuntimeException("Cidade não pode ser vazia");
        }

        if (endereco.getEstado() == null || endereco.getEstado().isBlank()) {
            throw new RuntimeException("Estado não pode ser vazio");
        }

        daoEndereco.salvar(endereco);
        return endereco;
    }

    public Endereco buscarPorId(Long id) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco endereco = daoEndereco.buscarPorId(id);

        if (endereco == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        return endereco;
    }

    public List<Endereco> listarTodos() {
        return daoEndereco.listarTodos();
    }

    public Endereco atualizar(Long id, Endereco endereco) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco existente = daoEndereco.buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        if (endereco.getCep() == null || endereco.getCep().isBlank()) {
            throw new RuntimeException("CEP não pode ser vazio");
        }

        if (endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()) {
            throw new RuntimeException("Logradouro não pode ser vazio");
        }

        if (endereco.getNumero() == null || endereco.getNumero().isBlank()) {
            throw new RuntimeException("Número não pode ser vazio");
        }

        if (endereco.getBairro() == null || endereco.getBairro().isBlank()) {
            throw new RuntimeException("Bairro não pode ser vazio");
        }

        if (endereco.getCidade() == null || endereco.getCidade().isBlank()) {
            throw new RuntimeException("Cidade não pode ser vazia");
        }

        if (endereco.getEstado() == null || endereco.getEstado().isBlank()) {
            throw new RuntimeException("Estado não pode ser vazio");
        }

        endereco.setId(id);

        daoEndereco.atualizar(endereco);
        return endereco;
    }

    public void desativar(Long id) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Endereco existente = daoEndereco.buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("Endereço não encontrado com id: " + id);
        }

        daoEndereco.desativar(id);
    }
}