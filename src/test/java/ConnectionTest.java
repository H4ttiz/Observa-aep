import br.com.observaacao.config.ConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

class ConnectionTest {

    @Test
    void deveConectarNoBanco() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Assertions.assertNotNull(connection);
            Assertions.assertFalse(connection.isClosed());
        } catch (Exception e) {
            Assertions.fail("Erro na conexão: " + e.getMessage());
        }
    }
}