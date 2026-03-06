package br.com.observaacao.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {

    public static Connection getConnection() {
        try {
            Properties props = new Properties();

            String env = System.getProperty("env", "default");

            String[] filesToTry;

            if ("test".equals(env)) {
                filesToTry = new String[]{
                        "application-test.properties",
                        "application.properties"
                };
            } else {
                filesToTry = new String[]{
                        "application-local.properties",
                        "application.properties"
                };
            }

            boolean loaded = false;

            for (String file : filesToTry) {
                try (InputStream input = ConnectionFactory.class
                        .getClassLoader()
                        .getResourceAsStream(file)) {

                    if (input != null) {
                        props.load(input);
                        loaded = true;
                        break;
                    }
                }
            }

            if (!loaded) {
                throw new RuntimeException("Nenhum arquivo de configuração encontrado.");
            }

            return DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar no banco", e);
        }
    }
}