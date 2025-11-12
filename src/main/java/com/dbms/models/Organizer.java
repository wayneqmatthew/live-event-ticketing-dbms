package com.dbms.models;

import java.util.ArrayList;
import java.sql.*;

public class Organizer {
    
    public int organizer_id;
    public String region;
    public String country;
    public String city;
    
    public ArrayList<Integer> organizer_id_list = new ArrayList<Integer>();
    public ArrayList<String> region_list = new ArrayList<String>();
    public ArrayList<String> country_list = new ArrayList<String>();
    public ArrayList<String> city_list = new ArrayList<String>();
    
    public Organizer(){}
    
    public int add_organizer(){
        String url = "jdbc:mysql://localhost:3306/ticket_system";
        String user = "root";
        String password = "bruh";
        
        try {
            // Connect to DB
            Connection conn = DriverManager.getConnection(url, user, password);
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticket_system?useTimezone=true&serverTimezone=UTC&user=root&password=bruh");
            System.out.println("Connection Successful");
            // Make a SQL statement
            // Generate the new id
            PreparedStatement pst = conn.prepareStatement("SELECT MAX(organizer_id) + 1 AS newID FROM organizer");
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                organizer_id = rst.getInt("newID");
            }

            // Add the new organizer to the DB
            pst = conn.prepareStatement("INSERT INTO organizer (organizer_id, region, country, city) VALUES (?, ?, ?, ?)");
            pst.setInt(1, organizer_id);
            pst.setString(2, region);
            pst.setString(3, country);
            pst.setString(4, city);
            pst.executeUpdate();

            pst.close();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
    // connect to DB to remove a specific organizer
    public int remove_organizer(){
        try{
           Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ticket_system?useTimezone=true&serverTimezone=UTC&user=root&password=bruh");
           PreparedStatement pst = conn.prepareStatement("DELETE FROM organizer WHERE organizer_id = ?");
           pst.setInt(1, organizer_id);
           pst.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }
    
    public static void main(String args[]){
        Organizer org = new Organizer();
        org.region = "New York";
        org.country = "USA";
        org.city = "New York City";
        org.add_organizer();
        System.out.println("Main executed");
        
    }
}
