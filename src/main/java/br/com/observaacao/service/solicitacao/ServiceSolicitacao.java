package br.com.observaacao.service.solicitacao;

import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;

import java.util.List;

public class ServiceSolicitacao {

    private final DaoSolicitacao daoSolicitacao;

    public ServiceSolicitacao(DaoSolicitacao daoSolicitacao) {
        this.daoSolicitacao = daoSolicitacao;
    }

    public Solicitacao cadastrar(Solicitacao solicitacao) {

        if (solicitacao.getId_categoria() == null) {
            throw new RuntimeException("Categoria não pode ser nula");
        }

        if (solicitacao.getId_solicitante() == null) {
            throw new RuntimeException("Solicitante não pode ser nulo");
        }

        if (solicitacao.getId_endereco() == null) {
            throw new RuntimeException("Endereço não pode ser nulo");
        }

        if (solicitacao.getTitulo() == null || solicitacao.getTitulo().isBlank()) {
            throw new RuntimeException("Título não pode ser vazio");
        }

        if (solicitacao.getDescricao() == null || solicitacao.getDescricao().isBlank()) {
            throw new RuntimeException("Descrição não pode ser vazia");
        }

        if (solicitacao.getStatus() == null) {
            throw new RuntimeException("Status não pode ser vazio");
        }

        if (solicitacao.getPrioridade() == null) {
            throw new RuntimeException("Prioridade não pode ser vazia");
        }

        if (solicitacao.getDt_solicitada() == null) {
            throw new RuntimeException("Data de solicitação não pode ser nula");
        }

        if (solicitacao.getDt_prazo() == null) {
            throw new RuntimeException("Data de prazo não pode ser nula");
        }

        daoSolicitacao.salvar(solicitacao);
        return solicitacao;
    }

    public Solicitacao buscarPorId(Long id) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Solicitacao solicitacao = daoSolicitacao.buscarPorId(id);

        if (solicitacao == null) {
            throw new RuntimeException("Solicitação não encontrada com id: " + id);
        }

        return solicitacao;
    }

    public List<Solicitacao> listarTodos() {
        return daoSolicitacao.listarTodos();
    }

    public Solicitacao atualizar(Long id, Solicitacao solicitacao) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Solicitacao existente = daoSolicitacao.buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("Solicitação não encontrada com id: " + id);
        }

        if (solicitacao.getId_categoria() == null) {
            throw new RuntimeException("Categoria não pode ser nula");
        }

        if (solicitacao.getId_solicitante() == null) {
            throw new RuntimeException("Solicitante não pode ser nulo");
        }

        if (solicitacao.getId_endereco() == null) {
            throw new RuntimeException("Endereço não pode ser nulo");
        }

        if (solicitacao.getTitulo() == null || solicitacao.getTitulo().isBlank()) {
            throw new RuntimeException("Título não pode ser vazio");
        }

        if (solicitacao.getDescricao() == null || solicitacao.getDescricao().isBlank()) {
            throw new RuntimeException("Descrição não pode ser vazia");
        }

        if (solicitacao.getStatus() == null) {
            throw new RuntimeException("Status não pode ser vazio");
        }

        if (solicitacao.getPrioridade() == null) {
            throw new RuntimeException("Prioridade não pode ser vazia");
        }

        if (solicitacao.getDt_solicitada() == null) {
            throw new RuntimeException("Data de solicitação não pode ser nula");
        }

        if (solicitacao.getDt_prazo() == null) {
            throw new RuntimeException("Data de prazo não pode ser nula");
        }

        solicitacao.setId(id);

        daoSolicitacao.atualizar(solicitacao);
        return solicitacao;
    }

    public void desativar(Long id) {

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo");
        }

        Solicitacao existente = daoSolicitacao.buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("Solicitação não encontrada com id: " + id);
        }

        daoSolicitacao.desativar(id);
    }
}