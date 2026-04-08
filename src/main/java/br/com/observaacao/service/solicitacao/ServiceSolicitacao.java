package br.com.observaacao.service.solicitacao;

import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;

import java.util.ArrayList;
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

        if (solicitacao.getDt_solicitada() == null) {
            throw new RuntimeException("Data de solicitação não pode ser nula");
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
            throw new RuntimeException("O identificador da solicitação é obrigatório para realizar a busca.");
        }

        Solicitacao existente = daoSolicitacao.buscarPorId(id);

        if (existente == null) {
            throw new RuntimeException("Não foi possível encontrar uma solicitação com o código informado: " + id);
        }

        if (solicitacao.getId_categoria() == null) {
            throw new RuntimeException("É necessário selecionar uma categoria para prosseguir.");
        }

        if (solicitacao.getId_solicitante() == null) {
            throw new RuntimeException("A solicitação precisa estar vinculada a um usuário solicitante.");
        }

        if (solicitacao.getId_endereco() == null) {
            throw new RuntimeException("As informações de localização são obrigatórias.");
        }

        if (solicitacao.getTitulo() == null || solicitacao.getTitulo().isBlank()) {
            throw new RuntimeException("O campo de título deve ser preenchido.");
        }

        if (solicitacao.getDescricao() == null || solicitacao.getDescricao().isBlank()) {
            throw new RuntimeException("Por favor, forneça uma descrição detalhada do problema.");
        }

        if (solicitacao.getStatus() == null) {
            throw new RuntimeException("O status da solicitação não foi definido corretamente.");
        }

        if (solicitacao.getDt_solicitada() == null) {
            throw new RuntimeException("A data de registro da solicitação está ausente.");
        }

        if (solicitacao.getObservacao() == null || solicitacao.getObservacao().isBlank()) {
            throw new RuntimeException("Para concluir esta ação, é obrigatório registrar uma observação ou justificativa.");
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

    public List<Solicitacao> buscarPorUsuario(Long idUsuario) {
        if (idUsuario == null) {
            throw new RuntimeException("Erro de identificação: Usuário inválido.");
        }
        return daoSolicitacao.listaPorUsuario(idUsuario);
    }

    public List<Solicitacao> buscarSolicitacaoPendente() {
        try {
            List<Solicitacao> solicitacoes = daoSolicitacao.buscarSolicitacaoPendente();

            if (solicitacoes == null) {
                throw new RuntimeException("Não foi possível carregar as pendências. Tente novamente mais tarde.");
            }

            return solicitacoes;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao buscar solicitações pendentes: " + e.getMessage());
        }
    }


}