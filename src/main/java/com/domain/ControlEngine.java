package com.domain;

import com.constants.FuelLevel;

public interface ControlEngine extends Subscribe{
    boolean startOn();
    boolean startOff();

    void increaseTorque();
    void decreaseTorque();
    FuelLevel checkFuelLevel();
    void consumeFuel(float speed);
    float getFuelConsumption(float speed);

    void refuel(float fuel);
    void refuel();
}
