package com.domain;

import com.constants.Location;
import com.domain.impl.Body;
import com.exception.CarException;

public interface ControlCar {
    void insertKey();
    void removeKey();
    void turnOnKey();
    void turnBackKey();

    void pressGas(int time);
    void releaseGas(int time);
    void pressBrake(int time);
    void releaseBrake(int time);

    void steerLeft();
    void steerRight();
    void steerStraight();

    void upGear();
    void downGear();

    void putCarInPark();
    void putCarInDrive();
    void putCarInReverse();

    void checkReadiness();

    void diagnostic(boolean showAll);

    float refuel();
    float refuel(float fuel);

    void openDoorInside(Location location);
    void openDoorOutside(Location location);
}
