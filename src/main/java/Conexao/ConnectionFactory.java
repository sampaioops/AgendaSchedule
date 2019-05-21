package Conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/agenda?useSSL=false", "root", "senha");

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
