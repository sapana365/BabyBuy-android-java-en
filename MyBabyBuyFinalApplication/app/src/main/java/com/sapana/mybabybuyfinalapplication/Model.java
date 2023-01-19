package com.sapana.mybabybuyfinalapplication;

public class Model {

    private int id;
    private String username;
    private String price;
    private String location;
    private String details;
    private String isPurchased;
    private byte[]proavatar;


    //constructor

    public Model(int id,String username,String price, String location, String details,String isPurchased, byte[] proavatar) {
        this.id = id;
        this.username = username;
        this.price = price;
        this.location = location;
        this.details= details;
        this.isPurchased = isPurchased;
        this.proavatar = proavatar;
    }
    //getter and setter method

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public String getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        this.isPurchased = isPurchased;
    }
    public byte[] getProavatar() {
        return proavatar;
    }

    public void setProavatar(byte[] proavatar) {
        this.proavatar = proavatar;
    }

}


