package com.domain;

import com.constants.Location;
import com.constants.VehicleLight;

public interface ControlBody extends Subscribe{
    boolean checkLights();
    void switchOffLight();
    void switchOnLight(VehicleLight vehicleLight);
    void switchOffLight(VehicleLight vehicleLight);
    void lockAllDoors();
    void openDoorInside(Location location);
    void openDoorOutside(Location location);
}
