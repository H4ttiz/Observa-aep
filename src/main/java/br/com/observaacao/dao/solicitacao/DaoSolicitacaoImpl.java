package br.com.observaacao.dao.solicitacao;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.enums.NivelPrioridade;
import br.com.observaacao.model.solicitacao.Solicitacao;
import br.com.observaacao.model.enums.StatusSolicitacao;
import br.com.observaacao.model.usuario.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoSolicitacaoImpl implements DaoSolicitacao {

    private static final String TABELA = "solicitacoes";

    private Solicitacao map(ResultSet rs) throws SQLException {
        String prioridadeStr = rs.getString("prioridade");
        NivelPrioridade prioridade = prioridadeStr != null
                ? NivelPrioridade.valueOf(prioridadeStr)
                : null;

        return new Solicitacao(
                rs.getLong("id"),
                rs.getLong("id_categoria"),
                rs.getLong("id_solicitante"),
                rs.getObject("id_atendente", Long.class),
                rs.getLong("id_endereco"),
                StatusSolicitacao.valueOf(rs.getString("status")),
                prioridade,
                rs.getBoolean("anonimo"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getObject("data_solicitada", LocalDateTime.class),
                rs.getObject("data_prazo", LocalDateTime.class),
                rs.getObject("data_finalizada", LocalDateTime.class),
                rs.getObject("observacao", String.class)
        );
    }

    @Override
    public Long salvar(Solicitacao solicitacao) {
        String sql = "INSERT INTO " + TABELA +
                " (id_categoria, id_solicitante, id_atendente, id_endereco, status, prioridade, anonimo, titulo, descricao, data_solicitada, data_prazo, data_finalizada,observacao) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setLong(1, solicitacao.getId_categoria());
            stmt.setLong(2, solicitacao.getId_solicitante());
            stmt.setObject(3, solicitacao.getId_atendente());
            stmt.setLong(4, solicitacao.getId_endereco());
            stmt.setString(5, solicitacao.getStatus().name());

            if (solicitacao.getPrioridade() != null) {
                stmt.setString(6, solicitacao.getPrioridade().name());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.setBoolean(7, solicitacao.getAnonimo());
            stmt.setString(8, solicitacao.getTitulo());
            stmt.setString(9, solicitacao.getDescricao());
            stmt.setObject(10, solicitacao.getDt_solicitada());
            stmt.setObject(11, solicitacao.getDt_prazo());
            stmt.setObject(12, solicitacao.getDt_finalizada());
            stmt.setObject(13, solicitacao.getObservacao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    solicitacao.setId(idGerado);
                    return idGerado;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("O sistema encontrou uma falha ao processar sua solicitação. Por favor, tente novamente em instantes.");
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
            throw new RuntimeException("Não foi possível carregar os dados desta solicitação agora. (Status 500 - Erro no Servidor)");
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
            throw new RuntimeException("Ops! Tivemos um problema ao listar as solicitações. Aguarde a manutenção do sistema.");
        }
    }

    @Override
    public void atualizar(Solicitacao solicitacao) {
        String sql = "UPDATE " + TABELA +
                " SET id_categoria = ?, id_solicitante = ?, id_atendente = ?, id_endereco = ?, status = ?, prioridade = ?, anonimo = ?, titulo = ?, descricao = ?, data_solicitada = ?, data_prazo = ?, data_finalizada = ? , observacao = ? " +
                " WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, solicitacao.getId_categoria());
            stmt.setLong(2, solicitacao.getId_solicitante());
            stmt.setObject(3, solicitacao.getId_atendente());
            stmt.setLong(4, solicitacao.getId_endereco());
            stmt.setString(5, solicitacao.getStatus().name());

            if (solicitacao.getPrioridade() != null) {
                stmt.setString(6, solicitacao.getPrioridade().name());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.setBoolean(7, solicitacao.getAnonimo());
            stmt.setString(8, solicitacao.getTitulo());
            stmt.setString(9, solicitacao.getDescricao());
            stmt.setObject(10, solicitacao.getDt_solicitada());
            stmt.setObject(11, solicitacao.getDt_prazo());
            stmt.setObject(12, solicitacao.getDt_finalizada());
            stmt.setObject(13, solicitacao.getObservacao());
            stmt.setLong(14, solicitacao.getId());

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                throw new RuntimeException("Não foi possível encontrar este registro para atualização. Ele pode ter sido removido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro interno no servidor: Falha ao atualizar os dados. Tente mais tarde.");
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
                throw new RuntimeException("Falha na operação: O registro solicitado não existe no sistema.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Serviço temporariamente indisponível. Aguarde a manutenção do servidor.");
        }
    }

    @Override
    public List<Solicitacao> listaPorUsuario(Long idUsuario) {

        String sql = "SELECT * FROM " + TABELA + " WHERE id_solicitante = ?";
        List<Solicitacao> solicitacoes = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    solicitacoes.add(map(rs));
                }
            }

            return solicitacoes;

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível carregar seu histórico de solicitações agora. (Status 500 - Erro no Servidor)");
        }
    }

    @Override
    public List<Solicitacao> listaPorAtendente(Long idAtendente) {

        String sql = "SELECT * FROM " + TABELA + " WHERE id_atendente = ? ORDER BY data_prazo ASC";
        List<Solicitacao> solicitacoes = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setLong(1, idAtendente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    solicitacoes.add(map(rs));
                }
            }

            return solicitacoes;

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível carregar seu histórico de solicitações agora. (Status 500 - Erro no Servidor)");
        }
    }

    @Override
    public List<Solicitacao> buscarSolicitacaoEspecifica(StatusSolicitacao statusSolicitacao) {
        String sql = "SELECT * FROM " + TABELA + " WHERE status = ? ORDER BY data_prazo ASC";
        List<Solicitacao> solicitacoes = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, statusSolicitacao.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    solicitacoes.add(map(rs));
                }
            }
            return solicitacoes;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao filtrar solicitações por prazo.");
        }
    }
}