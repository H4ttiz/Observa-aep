package br.com.observaacao.dao.categoria;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.categoria.Categoria;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoCategoriaImpl implements DaoCategoria{

    private static final String TABELA = "categorias";

    private Categoria map(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getLong("id"),
                rs.getString("categoria"),
                rs.getString("descricao"),
                rs.getObject("data_criacao", LocalDateTime.class),
                rs.getBoolean("ativo")
        );
    }


    @Override
    public Long salvar(Categoria categoria) {
        String sql = """
            INSERT INTO\s""" + TABELA + """
            (categoria, descricao, data_Criacao, ativo)
            VALUES (?, ?, ?, ?)
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, categoria.getCategoria());
            stmt.setString(2, categoria.getDescricao());
            stmt.setObject(3, categoria.getDataCriacao());
            stmt.setBoolean(4, categoria.isAtivo());


            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    categoria.setId(idGerado);
                    return idGerado;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu uma falha ao registrar a categoria. (Status 500 - Erro no Servidor)");
        }
    }

    @Override
    public Categoria buscarPorId(Long id) {
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
            throw new RuntimeException("Não foi possível localizar esta categoria. O sistema pode estar em manutenção.");
        }
    }

    @Override
    public List<Categoria> listarTodos() {

        String sql = "SELECT * FROM " + TABELA;

        List<Categoria> categorias = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                categorias.add(map(rs));
            }

            return categorias;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar lista de categorias. Tente novamente em instantes.");
        }
    }

    @Override
    public void atualizar(Categoria categoria) {
        String sql = "UPDATE " + TABELA + " SET categoria = ?, descricao = ?, ativo = ? WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, categoria.getCategoria());
            stmt.setString(2, categoria.getDescricao());
            stmt.setBoolean(3, categoria.isAtivo());
            stmt.setLong(4, categoria.getId());

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("A categoria solicitada não foi encontrada para atualização.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Falha interna ao atualizar categoria: " + e.getMessage());
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
                throw new RuntimeException("Operação inválida: Categoria inexistente.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Serviço temporariamente indisponível. Erro 500.");
        }
    }

    @Override
    public void ativar(Long id) {
        String sql = "UPDATE " + TABELA + " SET ativo = true WHERE id = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Operação inválida: Categoria inexistente.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Serviço temporariamente indisponível. Erro 500.");
        }
    }

    @Override
    public List<Categoria> listarTodosAtivas() {
        String sql = "SELECT * FROM " + TABELA + " WHERE ativo = true";

        List<Categoria> categorias = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                categorias.add(map(rs));
            }

            return categorias;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar lista de categorias. Tente novamente em instantes.");
        }
    }
}
