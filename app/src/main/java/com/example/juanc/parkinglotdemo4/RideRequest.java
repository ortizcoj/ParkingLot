package com.example.juanc.parkinglotdemo4;

import java.sql.Time;

public class RideRequest {
    private Time pickupTime;
    private String dropLot;
    private String pickupPlace;

    public RideRequest(Time pickupHour, String dropoffLot, String pickupLocation) {
        pickupTime = pickupHour;
        dropLot = dropoffLot;
        pickupPlace = pickupLocation;
    }
}
