package br.com.observaacao.service.solicitacao;

import br.com.observaacao.dao.solicitacao.DaoSolicitacao;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.usuario.Usuario;
import br.com.observaacao.service.historico_movimentacao_solicitacao.ServiceHistoricoMovimentacaoSolicitacao;
import br.com.observaacao.service.log.ServiceLog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceSolicitacao {

    private final DaoSolicitacao daoSolicitacao;
    private final ServiceHistoricoMovimentacaoSolicitacao serviceHistorico;
    private final ServiceLog logService;

    public ServiceSolicitacao(DaoSolicitacao daoSolicitacao,
                              ServiceHistoricoMovimentacaoSolicitacao serviceHistorico,
                              ServiceLog logService) {
        this.daoSolicitacao = daoSolicitacao;
        this.serviceHistorico = serviceHistorico;
        this.logService = logService;
    }

    public Solicitacao cadastrar(Solicitacao solicitacao) {
        validarSolicitacao(solicitacao);

        Long idGerado = daoSolicitacao.salvar(solicitacao);

        serviceHistorico.addHistoricoMovimentacaoSolicitacao(
                idGerado,
                null,
                StatusSolicitacao.N1,
                solicitacao.getId_solicitante(),
                "Solicitação registrada com sucesso no sistema."
        );

        String detalhes = String.format(
                "{\"titulo\": \"%s\", \"categoria_id\": %d, \"anonimo\": %b}",
                solicitacao.getTitulo(), solicitacao.getId_categoria(), solicitacao.getAnonimo()
        );
        logService.registrarLog(solicitacao.getId_solicitante(), "solicitacoes", "INSERT", detalhes);

        return solicitacao;
    }

    public Solicitacao atualizar(Long id, Solicitacao solicitacao, Usuario responsavel) {
        if (id == null) {
            throw new RuntimeException("O identificador da solicitação é obrigatório para realizar a busca.");
        }

        Solicitacao existente = daoSolicitacao.buscarPorId(id);
        if (existente == null) {
            throw new RuntimeException("Não foi possível encontrar uma solicitação com o código informado: " + id);
        }

        validarSolicitacao(solicitacao);

        if (solicitacao.getObservacao() == null || solicitacao.getObservacao().isBlank()) {
            throw new RuntimeException("Para concluir esta ação, é obrigatório registrar uma observação ou justificativa.");
        }

        StatusSolicitacao statusAnterior = existente.getStatus();
        solicitacao.setId(id);
        daoSolicitacao.atualizar(solicitacao);

        serviceHistorico.addHistoricoMovimentacaoSolicitacao(
                id,
                statusAnterior,
                solicitacao.getStatus(),
                responsavel.getId(),
                solicitacao.getObservacao()
        );

        String motivoSeguro = solicitacao.getObservacao()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String detalhes = String.format(
                "{\"id\": %d, \"status_de\": \"%s\", \"status_para\": \"%s\", \"motivo\": \"%s\"}",
                id, statusAnterior, solicitacao.getStatus(), motivoSeguro
        );

        logService.registrarLog(responsavel.getId(), "solicitacoes", "UPDATE", detalhes);

        return solicitacao;
    }

    private void validarSolicitacao(Solicitacao s) {
        if (s.getId_categoria() == null) throw new RuntimeException("Categoria não pode ser nula");
        if (s.getId_solicitante() == null) throw new RuntimeException("Solicitante não pode ser nulo");
        if (s.getId_endereco() == null) throw new RuntimeException("Endereço não pode ser nulo");
        if (s.getTitulo() == null || s.getTitulo().isBlank()) throw new RuntimeException("Título não pode ser vazio");
        if (s.getDescricao() == null || s.getDescricao().isBlank()) throw new RuntimeException("Descrição não pode ser vazia");
        if (s.getStatus() == null) throw new RuntimeException("Status não pode ser vazio");
        if (s.getDt_solicitada() == null) throw new RuntimeException("Data de solicitação não pode ser nula");
    }

    public Solicitacao buscarPorId(Long id) {
        if (id == null) throw new RuntimeException("ID não pode ser nulo");
        Solicitacao solicitacao = daoSolicitacao.buscarPorId(id);
        if (solicitacao == null) throw new RuntimeException("Solicitação não encontrada com id: " + id);
        return solicitacao;
    }

    public List<Solicitacao> listarTodos() {
        return daoSolicitacao.listarTodos();
    }

    public List<Solicitacao> buscarPorUsuario(Long idUsuario) {
        if (idUsuario == null) throw new RuntimeException("Erro de identificação: Usuário inválido.");
        return daoSolicitacao.listaPorUsuario(idUsuario);
    }

    public List<Solicitacao> buscarPorAtendente(Long idAtendente) {
        if (idAtendente == null) throw new RuntimeException("Erro de identificação: Usuário inválido.");
        return daoSolicitacao.listaPorAtendente(idAtendente);
    }

    public List<Solicitacao> buscarSolicitacaoPendente() {
        return daoSolicitacao.buscarSolicitacaoEspecifica(StatusSolicitacao.N1);
    }

    public List<Solicitacao> buscarSolicitacaoAguardandoAtendimento() {
        return daoSolicitacao.buscarSolicitacaoEspecifica(StatusSolicitacao.N3);
    }

    public List<Solicitacao> buscarSolicitacaoEmAndamento() {
        return daoSolicitacao.buscarSolicitacaoEspecifica(StatusSolicitacao.N4);
    }

    public List<Solicitacao> buscarSolicitacaoAtrasadas() {
        List<Solicitacao> solicitacoes = daoSolicitacao.listarTodos();
        List<Solicitacao> atrasadas = new ArrayList<>();

        for (Solicitacao s : solicitacoes) {
            if (s.getDt_prazo() != null &&
                    s.getDt_prazo().isBefore(LocalDateTime.now()) &&
                    (s.getStatus() == StatusSolicitacao.N4 || s.getStatus() == StatusSolicitacao.N3)) {
                atrasadas.add(s);
            }
        }
        return atrasadas;
    }
}