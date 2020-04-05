package com;

import com.domain.ControlCar;
import com.exception.CarException;
import com.service.CarFactory;
import static com.constants.Location.FRONT_LEFT;
import static com.constants.Location.FRONT_RIGHT;

public class Application {
    public static void main(String[] args) throws CarException {
        ControlCar car = CarFactory.buildCar("Qashqai");
        moving(car, 10);
        car = CarFactory.buildCar("Tesla");
        moving(car, 10000);
    }

    private static void moving(ControlCar car, int fuel){
        car.insertKey();
        car.turnOnKey();

        car.putCarInPark();
        car.putCarInDrive();
        car.putCarInReverse();

        car.pressGas(1);

        car.openDoorInside(FRONT_LEFT);
        car.openDoorOutside(FRONT_RIGHT);

        car.releaseGas(1);
        car.upGear();
        car.pressGas(1);
        car.releaseGas(1);
        car.pressGas(9);

        car.putCarInPark();

        car.diagnostic(false);
        car.refuel(10);
        car.turnOnKey();
        car.refuel();
        car.diagnostic(false);
        car.upGear();
        car.pressGas(2);
        car.upGear();
        car.pressGas(2);
        car.pressBrake(1);
        car.releaseBrake(1);
        car.steerLeft();
        car.steerRight();
        car.steerRight();
        car.steerStraight();
        car.steerRight();
        car.diagnostic(true);
    }
}
