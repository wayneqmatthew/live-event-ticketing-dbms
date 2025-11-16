package com.dbms.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Organizer {
    private int organizer_id;
    private String name;
    private String email;
    private String city;
    private String country;
    private String region;
    private String status;

    public Organizer(String name,String email,String city, String country,String region, String status){
        this.name = name;
        this.email = email;
        this.city = city;
        this.country = country;
        this.region = region;
        this.status = status;
    }

    public int add_organizer(){
        try (Connection conn = ConnectorDB.connect()) {
            if (conn == null) {
                System.out.println("Error: connection db");
                return 1;
            }

             // Generate new venue_id
            String idQuery = "SELECT MAX(organizer_id) + 1 AS newID FROM organizer";
            try (PreparedStatement pst = conn.prepareStatement(idQuery);
                ResultSet rst = pst.executeQuery()) {

                if (rst.next()) {
                    organizer_id = rst.getInt("newID");
                }
            }

            // add new organizer row
            String addQuery = "INSERT INTO organizer (organizer_id, name, email, city, country, region, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(addQuery)) {
                pst.setInt(1, organizer_id);
                pst.setString(2, name);
                pst.setString(3, email);
                pst.setString(4, city);
                pst.setString(5, country);
                pst.setString(6, region);
                pst.setString(7, status);
                
                pst.executeUpdate();
                System.out.println("organizer added");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 1;
        }
        return 0;
    }

}
