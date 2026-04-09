package br.com.observaacao.dao.usuario;

import br.com.observaacao.model.enums.TipoUsuario;
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
                rs.getObject("criado_por", Long.class),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("email"),
                rs.getString("senha"),
                TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                rs.getObject("data_criacao", LocalDateTime.class),
                rs.getBoolean("ativo")
        );
    }

    @Override
    public Long salvar(Usuario usuario) {

        String sql = """
            INSERT INTO\s""" + TABELA + """
            (criado_por, nome, cpf, email, senha, tipo_usuario, data_criacao, ativo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {
            stmt.setObject(1, usuario.getCriadoPor());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getSenha());
            stmt.setString(6, usuario.getTipoUsuario().name());
            stmt.setObject(7, usuario.getDataCriacao());
            stmt.setBoolean(8, usuario.isAtivo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    usuario.setId(idGerado);
                    return idGerado;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível concluir seu cadastro. (Erro 500 - Falha no Servidor)");
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
            throw new RuntimeException("Erro ao carregar dados do usuário. O serviço pode estar temporariamente indisponível.");
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
            throw new RuntimeException("Não foi possível recuperar a lista de usuários. Aguarde a manutenção do sistema.");
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
                throw new RuntimeException("Os dados não puderam ser atualizados: Usuário não localizado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro interno ao atualizar perfil. Tente novamente em alguns minutos.");
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
                throw new RuntimeException("Falha na operação: Este perfil de usuário já não existe ou já está inativo.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro no servidor ao tentar desativar conta. (Status 500)");
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
            throw new RuntimeException("Falha na autenticação: Erro ao verificar credenciais no servidor.");
        }
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM " + TABELA + " WHERE cpf = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao validar CPF. Por favor, verifique sua conexão ou tente mais tarde.");
        }
    }


}