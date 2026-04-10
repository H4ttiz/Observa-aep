package br.com.observaacao.dao.log;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoLogImpl implements DaoLog {
    private final String TABELA = "logs";

    public void salvar(Log log) {
        String sql = "INSERT INTO " + TABELA + " (id_usuario, nome_tabela, acao, dados_alterados) VALUES (?, ?, ?, ?::jsonb)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, log.getIdUsuario());
            stmt.setString(2, log.getNomeTabela());
            stmt.setString(3, log.getAcao());
            stmt.setString(4, log.getDadosAlterados()); // A String formatada como JSON

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar log: " + e.getMessage());
        }
    }

    public List<Log> listarTodos() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABELA + " ORDER BY data_execucao DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Log l = new Log();
                l.setId(rs.getLong("id"));
                l.setIdUsuario(rs.getLong("id_usuario"));
                l.setNomeTabela(rs.getString("nome_tabela"));
                l.setAcao(rs.getString("acao"));
                l.setDadosAlterados(rs.getString("dados_alterados"));
                l.setDataExecucao(rs.getTimestamp("data_execucao").toLocalDateTime());
                logs.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logs: " + e.getMessage());
        }
        return logs;
    }
}
