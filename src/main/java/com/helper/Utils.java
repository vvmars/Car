package com.helper;

import com.constants.Drive;
import com.constants.Location;
import com.constants.NTransmission;
import com.domain.ControlEngine;
import com.domain.TripComputer;
import com.domain.impl.Wheel;
import java.util.Map;
import java.util.function.BiPredicate;

import static com.constants.Constants.*;
import static com.constants.Constants.MSG_PRINT_STATE;
import static com.constants.Drive.*;
import static com.constants.Location.*;

public class Utils {
    private static BiPredicate<Drive, Map.Entry<Location, Wheel>> relatedWheels =
            (drive, entry) -> drive == ALL_WHEEL ||
                    (drive == FRONT && (entry.getKey() == FRONT_LEFT || entry.getKey() == FRONT_RIGHT)) ||
                    (drive == REAR && (entry.getKey() == REAR_LEFT || entry.getKey() == REAR_RIGHT));

    public static void startRotated (Map<Location, Wheel> wheels){
        wheels.forEach((location, wheel) -> wheel.setRotated(true));
    }

    public static void stopRotated (Map<Location, Wheel> wheels){
        wheels.forEach((location, wheel) -> wheel.setRotated(false));
    }

    public static void changeAngleForSteerLeft(Drive drive, Map<Location, Wheel> wheels){
        wheels.entrySet().stream()
                .filter(entry -> relatedWheels.test(drive, entry))
                .forEach(entry -> entry.getValue().increaseAngle());
    }

    public static void changeAngleForSteerRight(Drive drive, Map<Location, Wheel> wheels){
        wheels.entrySet().stream()
                .filter(entry -> relatedWheels.test(drive, entry))
                .forEach(entry -> entry.getValue().decreaseAngle());
    }

    public static void changeAngleForSteerStraight(Drive drive, Map<Location, Wheel> wheels){
        wheels.entrySet().stream()
                .filter(entry -> relatedWheels.test(drive, entry))
                .forEach(entry -> entry.getValue().resetAngle());
    }

    public static int getRealAngle(Map<Location, Wheel> wheels){
        return wheels.values().stream()
                .mapToInt(Wheel::getAngle)
                .filter(angle -> angle != 0)
                .max()
                .orElse(0);
    }

    public static void printDiagnostic(TripComputer tripComputer, boolean power, int speed, int gas, int brake,
                           Map<Location, Wheel> wheels, NTransmission transmission, ControlEngine engine) {
        tripComputer.printOnDashboard(MSG_SEPARATOR2);
        tripComputer.printOnDashboard(MSG_PRINT_STATE);
        tripComputer.printOnDashboard(MSG_STATE_POWER, power);
        tripComputer.printOnDashboard(MSG_STATE_SPEED, speed);
        tripComputer.printOnDashboard(MSG_STATE_GAS, gas);
        tripComputer.printOnDashboard(MSG_STATE_BRAKE, brake);
        tripComputer.printOnDashboard(MSG_STATE_WHEELS);
        wheels.values().stream().forEach(wheel -> {
            tripComputer.printOnDashboard(MSG_STATE_WHEEL, wheel.getLocation());
            tripComputer.printOnDashboard(MSG_STATE_WHEEL_ROTATED, wheel.isRotated());
            tripComputer.printOnDashboard(MSG_STATE_WHEEL_ANGLE, wheel.getAngle());
        });
        tripComputer.printOnDashboard(MSG_STATE_TRANSMISSION, transmission);
        tripComputer.printOnDashboard(MSG_STATE_ENGINE_STARTED, engine.isStarted());
        tripComputer.printOnDashboard(MSG_STATE_ENGINE_TORQUE, engine.getTorque());
        tripComputer.printOnDashboard(MSG_STATE_ENGINE_FUEL, engine.getFuel());
        tripComputer.printOnDashboard(MSG_STATE_ENGINE_FUEL_CONSUMPTION, engine.getFuelConsumption());
        tripComputer.printOnDashboard(MSG_SEPARATOR2);
    }
}
