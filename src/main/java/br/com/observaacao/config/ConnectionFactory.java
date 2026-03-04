package br.com.obsevaacao.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream local = ConnectionFactory.class
                    .getClassLoader()
                    .getResourceAsStream("application-local.properties");
            if (local != null) {
                properties.load(local);
            } else {
                InputStream padrao = ConnectionFactory.class
                        .getClassLoader()
                        .getResourceAsStream("application.properties");

                properties.load(padrao);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar propriedades do banco", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
