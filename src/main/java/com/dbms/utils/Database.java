package com.dbms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/ticket_system";
    private static final String USER = "root";
    private static final String PASSWORD = "Gelker0921"; 

    /**
     * Connects to the database and returns the connection object.
     */
    public static Connection connect() throws SQLException {
        try {
            // This line loads the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // This line attempts to connect
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            // This happens if you forgot to add the MySQL connector to build.gradle
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}