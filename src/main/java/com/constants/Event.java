package com.constants;

public enum Event {
    DOOR_OPEN("door open"),
    DOOR_CLOSE("door close"),
    CRITICAL_FUEL("critical fuel level");

    private String event;
    Event(String event){
        this.event = event;
    }

    public String getValue() {
        return event;
    }
}
