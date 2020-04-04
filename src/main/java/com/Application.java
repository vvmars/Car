package com;

import com.domain.Car;
import com.service.CarFactory;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Car car = CarFactory.buildSedan();
        car.insertKey();
        car.turnOnKey();
        car.pressGas();
        car.pressGas();
    }

    private static void printLegend(){
        System.out.println("***************************************");
        System.out.println("1  - switch on engine");

        System.out.println("\\?  - Help");
        System.out.println("---------------------------------------");
        System.out.print("Please chose command - ");

        String command;
        Scanner scanner = new Scanner(System.in);;
        Car car = CarFactory.buildSedan();
        do {
            command = scanner.next();
            car.insertKey();
        }while(!command.equalsIgnoreCase("y") &&
                command.equalsIgnoreCase("1"));
    }
}
