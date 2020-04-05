package com;

import com.domain.ControlCar;
import com.exception.CarException;
import com.service.CarFactory;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws CarException {
        ControlCar car = CarFactory.buildCar("Sedan");
        car.insertKey();
        car.turnOnKey();
        car.pressGas(1);
        car.releaseGas(1);
        car.upGear();
        car.pressGas(1);
        car.releaseGas(1);
        car.pressGas(7);

        car.refuel(10);
        car.turnOnKey();
        car.refuel();
        car.diagnostic(false);

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

    private static void printLegend() throws CarException {
        System.out.println("***************************************");
        System.out.println("1  - switch on engine");

        System.out.println("\\?  - Help");
        System.out.println("---------------------------------------");
        System.out.print("Please chose command - ");

        String command;
        Scanner scanner = new Scanner(System.in);;
        ControlCar car = CarFactory.buildCar("Sedan");
        do {
            command = scanner.next();
            car.insertKey();
        }while(!command.equalsIgnoreCase("y") &&
                command.equalsIgnoreCase("1"));
    }
}
