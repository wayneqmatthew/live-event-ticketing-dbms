package com.dbms.models;

import java.sql.*;

public class ConnectorDB {
    private static final String URL = "jdbc:mysql://localhost:3306/ticket_system?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "bruh";

    public static Connection connect(){
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DB connected");
            return conn;
        } catch (SQLException e) {
            System.out.println("Error: ConnectorDB: " + e.getMessage());
            return null;
        }
    }

    public static PreparedStatement prepare(Connection conn, String sql, Object... params) throws SQLException {
    PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        return pst;
    }

    public static void close(Connection conn, PreparedStatement pst) {
    try {
        if (pst != null) pst.close();
        if (conn != null) conn.close();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            close(conn, pst);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

