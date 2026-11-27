package com.empresa.rh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://127.0.0.1:3000/DB_SA_SRH?useSSL=false&serverTimezone=UTC";
    // Substitua pelo seu usuário MySQL
    private static final String USER = "root";
    // Substitua pela sua senha MySQL
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            // Força o carregamento da classe do driver no ClassPath
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do MySQL não encontrado no classpath!", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}
