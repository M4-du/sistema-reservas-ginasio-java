package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/ginasio_db";
    private static final String USER = "root";
    private static final String PASSWORD = "92482912";

    public static Connection ligar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Erro na ligação: " + e.getMessage());
            return null;
        }
    }
}