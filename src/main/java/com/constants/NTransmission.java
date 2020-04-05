package com.constants;

public enum NTransmission {
    NEUTRAL("neutral"),
    IN_PARK("in park"),
    IN_DRIVE("in drive"),
    FIRST("first"),
    SECOND("second"),
    THIRD("third"),
    FOURTH("fourth"),
    FIFTH("fifth"),
    SIXTH("sixth"),
    REVERSE("reverse");

    private String nTransmission;
    NTransmission(String status){
        this.nTransmission = status;
    }

    public String getValue() {
        return nTransmission;
    }
}
