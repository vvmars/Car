package com.constants;

public enum FuelLevel {
    NORMAL("normal"),
    LOW("low"),
    CRITICAL("critical");

    private String fuelLevel;
    FuelLevel(String fuelLevel){
        this.fuelLevel = fuelLevel;
    }

    public String getValue() {
        return fuelLevel;
    }
}
