package br.com.observaacao.dao.usuario;

import br.com.observaacao.model.usuario.TipoUsuario;
import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.usuario.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Codigo SQL Usuario
public class DaoUsuarioImpl implements DaoUsuario {

    private static final String TABELA = "usuarios";

    private Usuario map(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha"),
                TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                rs.getObject("data_criacao", LocalDateTime.class),
                rs.getBoolean("ativo")
        );
    }

    @Override
    public void salvar(Usuario usuario) {

        String sql = """
            INSERT INTO\s""" + TABELA + """
            (nome, email, senha, tipo_usuario, data_criacao, ativo)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipoUsuario().name());
            stmt.setObject(5, usuario.getDataCriacao());
            stmt.setBoolean(6, usuario.isAtivo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    usuario.setId(idGerado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {

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
    public List<Usuario> listarTodos() {

        String sql = "SELECT * FROM " + TABELA;
        List<Usuario> usuarios = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                usuarios.add(map(rs));
            }

            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {

        String sql = """
            UPDATE\s""" + TABELA + """
            SET nome = ?,
                email = ?,
                senha = ?,
                tipo_usuario = ?,
                ativo = ?
            WHERE id = ?
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipoUsuario().name());
            stmt.setBoolean(5, usuario.isAtivo());
            stmt.setLong(6, usuario.getId());

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException(
                        "Nenhum usuário encontrado com id: " + usuario.getId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
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

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM " + TABELA + " WHERE email = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id", e);
        }
    }
}