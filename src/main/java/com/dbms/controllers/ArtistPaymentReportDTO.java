package com.dbms.controllers;

public class ArtistPaymentReportDTO {
    private int artist_id;
    private String artist_name;
    private float total_payment;

    public ArtistPaymentReportDTO(int artist_id, String artist_name, float total_payment){
        this.artist_id = artist_id;
        this.artist_name = artist_name;
        this.total_payment = total_payment;
    }

    public int getArtist_id(){return artist_id;}
    public String getArtist_name(){return artist_name;}
    public float getTotal_payment(){return total_payment;}
}
