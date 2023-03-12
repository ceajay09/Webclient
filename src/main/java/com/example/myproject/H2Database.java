package com.example.myproject;
import java.sql.*;

public class H2Database {
        public static void main(String[] args) throws ClassNotFoundException, SQLException {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");

            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT, name VARCHAR(255), email VARCHAR(255), PRIMARY KEY (id))");

            stmt.close();
            conn.close();
        }
    }

