package com.example.juanc.parkinglotdemo4;

import java.sql.Time;

public class DriveRequest {
    private Time pickupTime;
    private String dropLot;

    public DriveRequest(Time pickupHour, String dropoffLot){
        pickupTime = pickupHour;
        dropLot = dropoffLot;
    }
}
