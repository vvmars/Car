package com.service;

import com.constants.Location;
import com.domain.*;
import com.domain.impl.Car;
import com.domain.impl.PetrolEngine;
import com.domain.impl.Wheel;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import static com.constants.Drive.*;
import static com.constants.LightStatus.*;
import static com.constants.NTransmission.*;
import static com.constants.VehicleLight.*;

public class CarFactory {

    public static Car buildSedan(){
        PetrolEngine engine = new PetrolEngine(60, 120);
        engine.setPetrolLevel(2);

        return Car.builder()
                .withBody()
                    .withDoors(doorsBuilder())
                    .withCarLight(HEAD, new CarLight(OFF))
                    .withCarLight(HAZARD, new CarLight(OFF))
                    .withCarLight(BRAKE, new CarLight(OFF))
                    .withCarLight(DAYTIME_RUNNING, new CarLight(OFF))
                    .withCarLight(LEFT_SIGNAL, new CarLight(OFF))
                    .withCarLight(RIGHT_SIGNAL, new CarLight(OFF))
                    .done()
                .withEngine(engine)
                .withTransmission()
                    .withAutomatic(false)
                    .withTransmission(NEUTRAL)
                    .withDrive(FRONT)
                    .done()
                .withWheels(wheelsBuilder(14, 16))
                .withMaxSpeed(160)
                .withClearance(15)
                .build();
    }

    private static Map<Location, Door> doorsBuilder(){
        return Arrays.stream(Location.values())
                .map(location ->
                        Door.builder()
                                .withLocation(location)
                                .withStatusLock(false)
                                .withStatusOpen(true)
                                .build())
                .collect(Collectors.toMap(Door::getLocation, door -> door));
    }

    private static Map<Location, Wheel> wheelsBuilder(int innerRadius, int outerRadius){
        return Arrays.stream(Location.values())
                .map(location ->
                        Wheel.builder()
                                .withLocation(location)
                                .withInnerRadius(innerRadius)
                                .withOuterRadius(outerRadius)
                                .withAngle(0)
                                .withRotated(false)
                                .build())
                .collect(Collectors.toMap(Wheel::getLocation, wheel -> wheel));
    }
}
