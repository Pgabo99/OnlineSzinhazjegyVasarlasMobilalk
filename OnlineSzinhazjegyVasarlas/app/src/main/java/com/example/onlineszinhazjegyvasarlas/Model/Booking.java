package com.example.onlineszinhazjegyvasarlas.Model;

import java.util.UUID;

public class Booking {
    private String idPLAY;
    private String name;
    private String loggedInID;
    private String phoneNumber;
    private int seats;
    private String playName;
    private String date;

    public Booking(String name, String loggedInID, String phoneNumber, int seats, String playName, String date) {
        this.idPLAY= UUID.randomUUID().toString();
        this.name = name;
        this.loggedInID = loggedInID;
        this.phoneNumber = phoneNumber;
        this.seats = seats;
        this.playName = playName;
        this.date = date;
    }

    public Booking(String id, String name, String loggedInID, String phoneNumber, int seats, String playName, String date) {
        this.idPLAY = id;
        this.name = name;
        this.loggedInID = loggedInID;
        this.phoneNumber = phoneNumber;
        this.seats = seats;
        this.playName = playName;
        this.date = date;
    }

    public Booking() {
        this.idPLAY= UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getloggedInID() {
        return loggedInID;
    }

    public void setloggedInID(String loggedInID) {
        this.loggedInID = loggedInID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSeats() {
        return seats;
    }
    public String _getSeatsInString() {
        return String.valueOf(seats);
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return idPLAY;
    }

    public void _setId(String id) {
        this.idPLAY = id;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "idPLAY='" + idPLAY + '\'' +
                ", name='" + name + '\'' +
                ", loggedInID='" + loggedInID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", seats=" + seats +
                ", playName='" + playName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
