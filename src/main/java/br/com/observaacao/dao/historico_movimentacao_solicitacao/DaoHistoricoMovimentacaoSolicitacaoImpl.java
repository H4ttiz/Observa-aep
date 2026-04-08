package br.com.observaacao.dao.historico_movimentacao_solicitacao;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.historico_movimentacao_solicitacao.HistoricoMovimentacaoSolicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoHistoricoMovimentacaoSolicitacaoImpl implements DaoHistoricoMovimentacaoSolicitacao {

    private static final String TABELA = "historico_movimentacao_solicitacao";

    private HistoricoMovimentacaoSolicitacao map(ResultSet rs) throws SQLException {
        return new HistoricoMovimentacaoSolicitacao(
                rs.getLong("id"),
                rs.getLong("id_solicitacao"),
                rs.getLong("id_responsavel"),
                rs.getString("comentario"),
                StatusSolicitacao.valueOf(rs.getString("status_atual")),
                StatusSolicitacao.valueOf(rs.getString("status_anterior")),
                rs.getObject("data_movimentacao", LocalDateTime.class)
        );
    }

    @Override
    public void salvar(HistoricoMovimentacaoSolicitacao historico) {

        String sql = "INSERT INTO " + TABELA +
                " (id_solicitacao, id_responsavel, comentario, status_atual, status_anterior) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {
            stmt.setLong(1, historico.getId_solicitacao());
            stmt.setLong(2, historico.getId_responsavel());
            stmt.setString(3, historico.getComentario());
            stmt.setString(4, historico.getStatus_atual().name()); // Alterado para .name() para consistência com enums
            stmt.setString(5, historico.getStatus_anterior().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível registrar a movimentação no histórico. Por favor, tente novamente mais tarde.");
        }
    }

    @Override
    public HistoricoMovimentacaoSolicitacao buscarPorId(Long id) {

        String sql = "SELECT * FROM " + TABELA + " WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao recuperar dados do histórico. (Status 500 - Erro no Servidor)");
        }
    }

    @Override
    public List<HistoricoMovimentacaoSolicitacao> listarTodos() {

        String sql = "SELECT * FROM " + TABELA;
        List<HistoricoMovimentacaoSolicitacao> historicos = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                historicos.add(map(rs));
            }

            return historicos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro interno: Não foi possível listar o histórico de movimentações.");
        }
    }

    @Override
    public List<HistoricoMovimentacaoSolicitacao> buscarPorSolicitacao(Long idSolicitacao) {

        String sql = "SELECT * FROM " + TABELA +
                " WHERE id_solicitacao = ? ORDER BY data_movimentacao DESC";

        List<HistoricoMovimentacaoSolicitacao> historicos = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, idSolicitacao);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historicos.add(map(rs));
                }
            }

            return historicos;

        } catch (SQLException e) {
            throw new RuntimeException("Ops! Ocorreu um erro ao buscar o histórico desta solicitação. Aguarde a manutenção.");
        }
    }

    @Override
    public void atualizar(HistoricoMovimentacaoSolicitacao historico) {
        throw new UnsupportedOperationException(
                "Ação não permitida: Registros de histórico não podem ser alterados após o envio."
        );
    }

    @Override
    public void desativar(Long id) {
        throw new UnsupportedOperationException(
                "Ação não permitida: Registros de histórico não podem ser removidos por questões de auditoria."
        );
    }
}