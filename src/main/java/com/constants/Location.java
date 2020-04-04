package com.constants;

public enum Location {
    FRONT_LEFT("front left"),
    FRONT_RIGHT("front right"),
    REAR_LEFT("rear left"),
    REAR_RIGHT("rear right");

    private String doorEnum;
    Location(String doorEnum){
        this.doorEnum = doorEnum;
    }

    public String getValue() {
        return doorEnum;
    }
}
