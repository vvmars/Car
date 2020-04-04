package com.domain;

import com.constants.VehicleLight;

public interface ControlBody extends Subscribe{
    boolean checkLights();
    void switchOffLight();
    void switchOnLight(VehicleLight vehicleLight);
    void switchOffLight(VehicleLight vehicleLight);
}
