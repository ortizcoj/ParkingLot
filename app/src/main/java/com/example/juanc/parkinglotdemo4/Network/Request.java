package com.example.juanc.parkinglotdemo4.Network;

public class Request {

    private String email;
    private String startTime;
    private String endTime;
    private String pickupLocation;
    private String dropoffLot;

    public Request(String uemail, String ustartTime, String uendTime, String upickupLocation, String udropoffLot) {
        email = uemail;
        startTime = ustartTime;
        endTime = uendTime;
        pickupLocation = upickupLocation;
        dropoffLot = udropoffLot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLot() {
        return dropoffLot;
    }

    public void setDropoffLot(String dropoffLot) {
        this.dropoffLot = dropoffLot;
    }
}
