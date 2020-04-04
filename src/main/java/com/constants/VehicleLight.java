package com.constants;

public enum VehicleLight {
    HEAD("headlight"),
    HAZARD("hazard light"),
    BRAKE("brake light"),
    DAYTIME_RUNNING("daytime running light"),
    FOG("fog light"),
    LEFT_SIGNAL("left signal light"),
    RIGHT_SIGNAL("right signal light");

    private String status;
    VehicleLight(String status){
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}
