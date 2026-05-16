package br.com.observaacao.dao.endereco;

import br.com.observaacao.config.ConnectionFactory;
import br.com.observaacao.model.endereco.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoEnderecoImpl implements DaoEndereco {

    private static final String TABELA = "enderecos";

    private Endereco map(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getLong("id"),
                rs.getString("cep"),
                rs.getString("logradouro"),
                rs.getString("numero"),
                rs.getString("complemento"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("estado")
        );
    }

    @Override
    public Long salvar(Endereco endereco) {

        String sql = """
            INSERT INTO\s""" + TABELA + """
            (cep, logradouro, numero, complemento, bairro, cidade, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        sql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long idGerado = rs.getLong(1);
                    endereco.setId(idGerado);
                    return idGerado;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível salvar os dados de endereço. (Status 500 - Erro de Conexão)");
        }
    }

    @Override
    public Endereco buscarPorId(Long id) {

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
            throw new RuntimeException("Erro ao localizar endereço. O servidor pode estar em manutenção.");
        }
    }

    @Override
    public List<Endereco> listarTodos() {

        String sql = "SELECT * FROM " + TABELA;
        List<Endereco> endereco = new ArrayList<>();

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                endereco.add(map(rs));
            }

            return endereco;

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao carregar lista de endereços. Tente novamente em instantes.");
        }
    }

    @Override
    public void atualizar(Endereco endereco) {

        String sql = """
            UPDATE\s""" + TABELA + """
            SET nome = ?,
                cep = ?,
                logradouro = ?,
                numero = ?,
                complemento = ?,
                bairro = ?,
                cidade = ?,
                estado = ?
            WHERE id = ?
            """;

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setLong(6, endereco.getId());

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Não foi possível atualizar: Endereço não encontrado no sistema.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro interno no servidor ao atualizar o endereço. Aguarde a manutenção.");
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
                throw new RuntimeException("O endereço solicitado não foi localizado para desativação.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Serviço de banco de dados indisponível no momento. Erro 500.");
        }
    }

    @Override
    public Endereco buscarPorCep(String cep) {

        String sql = "SELECT * FROM " + TABELA + " WHERE cep = ?";

        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, cep);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar CEP. Verifique sua conexão com o servidor.");
        }
    }
}
