package com.constants;

public enum Drive {
    ALL_WHEEL("all-wheel drive"),
    FRONT("front drive"),
    REAR("rear drive");

    private String drive;
    Drive(String drive){
        this.drive = drive;
    }

    public String getValue() {
        return drive;
    }
}
