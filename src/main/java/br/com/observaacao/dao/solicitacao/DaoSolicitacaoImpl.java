package br.com.observaacao.dao.solicitacao;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.solicitacao.enums.NivelPrioridade;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.solicitacao.enums.StatusSolicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoSolicitacaoImpl implements DaoSolicitacao {

    private static final String TABELA = "solicitacao";


    private Solicitacao map(ResultSet rs) throws SQLException {
        return new Solicitacao(
                rs.getLong("id"),
                rs.getLong("id_categoria"),
                rs.getLong("id_solicitante"),
                rs.getLong("id_atendente"),
                rs.getLong("id_endereco"),
                StatusSolicitacao.valueOf(rs.getString("status")),
                NivelPrioridade.valueOf(rs.getString("prioridade")),
                rs.getBoolean("anonimo"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getObject("dt_solicitada", LocalDateTime.class),
                rs.getObject("dt_prazo", LocalDateTime.class),
                rs.getObject("dt_finalizada", LocalDateTime.class)
        );
    }

    @Override
    public void salvar(Solicitacao solicitacao) {

        String sql = """
        INSERT INTO\s""" + TABELA + """
        (id_categoria, id_solicitante, id_atendente, id_endereco, status, prioridade, anonimo, titulo, descricao, dt_solicitada, dt_prazo, dt_finalizada)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setLong(1, solicitacao.getId_categoria());
            stmt.setLong(2, solicitacao.getId_solicitante());
            stmt.setLong(3, solicitacao.getId_atendente());
            stmt.setLong(4, solicitacao.getId_endereco());
            stmt.setString(5, solicitacao.getStatus().name());
            stmt.setString(6, solicitacao.getPrioridade().name());
            stmt.setBoolean(7, solicitacao.getAnonimo());
            stmt.setString(8, solicitacao.getTitulo());
            stmt.setString(9, solicitacao.getDescricao());
            stmt.setObject(10, solicitacao.getDt_solicitada());
            stmt.setObject(11, solicitacao.getDt_prazo());
            stmt.setObject(12, solicitacao.getDt_finalizada());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    solicitacao.setId(idGerado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar solicitação", e);
        }
    }

    @Override
    public Solicitacao buscarPorId(Long id) {

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
            throw new RuntimeException("Erro ao buscar solicitação por id", e);
        }
    }

    @Override
    public List<Solicitacao> listarTodos() {

        String sql = "SELECT * FROM " + TABELA;
        List<Solicitacao> solicitacoes = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                solicitacoes.add(map(rs));
            }

            return solicitacoes;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar solicitações", e);
        }
    }

    @Override
    public void atualizar(Solicitacao solicitacao) {

        String sql = """
        UPDATE\s""" + TABELA + """
        SET id_categoria = ?,
            id_solicitante = ?,
            id_atendente = ?,
            id_endereco = ?,
            status = ?,
            prioridade = ?,
            anonimo = ?,
            titulo = ?,
            descricao = ?,
            dt_solicitada = ?,
            dt_prazo = ?,
            dt_finalizada = ?
        WHERE id = ?
        """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, solicitacao.getId_categoria());
            stmt.setLong(2, solicitacao.getId_solicitante());
            stmt.setLong(3, solicitacao.getId_atendente());
            stmt.setLong(4, solicitacao.getId_endereco());
            stmt.setString(5, solicitacao.getStatus().name());
            stmt.setString(6, solicitacao.getPrioridade().name());
            stmt.setBoolean(7, solicitacao.getAnonimo());
            stmt.setString(8, solicitacao.getTitulo());
            stmt.setString(9, solicitacao.getDescricao());
            stmt.setObject(10, solicitacao.getDt_solicitada());
            stmt.setObject(11, solicitacao.getDt_prazo());
            stmt.setObject(12, solicitacao.getDt_finalizada());
            stmt.setLong(13, solicitacao.getId());

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException(
                        "Nenhuma solicitação encontrada com id: " + solicitacao.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar solicitação", e);
        }
    }

    @Override
    public void desativar(Long id) {

        String sql = "UPDATE " + TABELA + " SET ativo = false WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException(
                        "Nenhuma solicitação encontrada com id: " + id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar solicitação", e);
        }
    }
}
