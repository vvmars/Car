package com.domain;

import com.domain.impl.Body;

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

    //PutCarInPark
    //void putCarInDrive();
    //PutCarInReverse
    void checkReadiness();

    void diagnostic(boolean showAll);

    float refuel();
    float refuel(float fuel);
}
