package br.com.observaacao.dao.categoria;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.categoria.Categoria;
import br.com.observaacao.model.categoria.NivelPrioridade;
import br.com.observaacao.model.usuario.Usuario;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DaoCategoriaimpl implements DaoCategoria{

    private static final String TABELA = "categorias";

    private Categoria map(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getLong("id"),
                rs.getString("categoria"),
                rs.getString("descricao"),
                NivelPrioridade.valueOf(rs.getString("nivel_Prioridade")),
                rs.getObject("data_criacao", LocalDateTime.class),
                rs.getBoolean("ativo")
        );
    }


    @Override
    public void salvar(Categoria categoria) {
        String sql = """
            INSERT INTO\s""" + TABELA + """
            (categoria, descricao, nivel_Prioridade, data_Criacao, ativo)
            VALUES (?, ?, ?, ?, ?)
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
            stmt.setString(3, categoria.getNivelPrioridade().name());
            stmt.setObject(4, categoria.getDataCriacao());
            stmt.setBoolean(5, categoria.isAtivo());


            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    categoria.setId(idGerado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
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
            throw new RuntimeException("Erro ao buscar usuário por id", e);
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
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    @Override
    public void atualizar(Categoria categoria) {
        String sql = """
            UPDATE\s""" + TABELA + """
            SET categoria = ?,
                descricao = ?,
                nivel_Prioridade = ?,
                ativo = ?
            WHERE id = ?
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, categoria.getCategoria());
            stmt.setString(2, categoria.getDescricao());
            stmt.setString(3, categoria.getNivelPrioridade().name());
            stmt.setBoolean(4, categoria.isAtivo());
            stmt.setLong(5, categoria.getId());

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException(
                        "Nenhum usuário encontrado com id: " + categoria.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria", e);
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
                        "Nenhum usuário encontrado com id: " + id
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar usuário", e);
        }
    }
}
