package com.domain;

import com.constants.FuelLevel;
import com.exception.CarException;

public interface ControlEngine extends Subscribe{
    boolean isStarted();
    void startOn() throws CarException;
    boolean startOff();
    void increaseTorque() throws CarException;
    void decreaseTorque() throws CarException;
    FuelLevel checkFuelLevel();
    void consumeFuel();
    float getFuelConsumption();
    float refuel(float fuel);
    float refuel();
    float getFuel();
    int getTorque();
    float getMaxFuelLevel();
}
