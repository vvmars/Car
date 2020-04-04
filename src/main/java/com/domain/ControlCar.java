package com.domain;

public interface ControlCar {
    void insertKey();
    void removeKey();
    void turnOnKey();
    void turnBackKey();

    void pressGas();
    void releaseGas();
    void pressBrake();
    void releaseBrake();

    void steerLeft();
    void steerRight();
    void steerStraight();

    void changeGear(int gear);
    //PutCarInPark
    void putCarInDrive();
    //PutCarInReverse
    void checkReadiness();

    void diagnostic();

    float refuel();
}
