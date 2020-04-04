package com.domain;

import com.constants.FuelLevel;

public interface ControlEngine extends Subscribe{
    boolean isStarted();
    boolean startOn();
    boolean startOff();

    void increaseTorque();
    void decreaseTorque();
    float getFuel();
    FuelLevel checkFuelLevel();
    void consumeFuel();
    float getFuelConsumption();

    int getTorque();

    void refuel(float fuel);
    void refuel();
}
