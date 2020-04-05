package com.service;

import com.constants.Location;
import com.domain.*;
import com.domain.impl.*;
import com.exception.CarException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import static com.constants.Drive.*;
import static com.constants.LightStatus.*;
import static com.constants.VehicleLight.*;
import static java.lang.String.format;

public class CarFactory {

    public static ControlCar buildCar(String model) throws CarException {
        switch(model){
            case "Qashqai": return buildSedan();
            case "Tesla": return buildTesla();
            default: throw new CarException(format("Model %s was not implemented", model));
        }
    }

    private static Car buildSedan(){
        PetrolEngine engine = new PetrolEngine(60, 120);
        engine.setPetrolLevel(2);

        return Car.builder()
                .withBody(Body.builder()
                        .withDoors(doorsBuilder())
                        .withCarLight(HEAD, new CarLight(OFF))
                        .withCarLight(HAZARD, new CarLight(OFF))
                        .withCarLight(BRAKE, new CarLight(OFF))
                        .withCarLight(DAYTIME_RUNNING, new CarLight(OFF))
                        .withCarLight(LEFT_SIGNAL, new CarLight(OFF))
                        .withCarLight(RIGHT_SIGNAL, new CarLight(OFF))
                        .build())
                .withEngine(engine)
                .withTransmission(Transmission.builder()
                        .withAutomatic(false)
                        .withDrive(FRONT)
                        .build())
                .withWheels(wheelsBuilder(14, 16))
                .withMaxSpeed(160)
                .withClearance(15)
                .build();
    }

    private static Car buildTesla(){
        ElectroEngine engine = new ElectroEngine(10000, 120);
        engine.setChargeLevel(500);

        return Car.builder()
                .withBody(Body.builder()
                        .withDoors(doorsBuilder())
                        .withCarLight(HEAD, new CarLight(OFF))
                        .withCarLight(HAZARD, new CarLight(OFF))
                        .withCarLight(BRAKE, new CarLight(OFF))
                        .withCarLight(FOG, new CarLight(OFF))
                        .withCarLight(DAYTIME_RUNNING, new CarLight(OFF))
                        .withCarLight(LEFT_SIGNAL, new CarLight(OFF))
                        .withCarLight(RIGHT_SIGNAL, new CarLight(OFF))
                        .build())
                .withEngine(engine)
                .withTransmission(Transmission.builder()
                        .withAutomatic(true)
                        .withDrive(ALL_WHEEL)
                        .build())
                .withWheels(wheelsBuilder(14, 16))
                .withMaxSpeed(190)
                .withClearance(15)
                .build();
    }

    private static Map<Location, Door> doorsBuilder(){
        return Arrays.stream(Location.values())
                .map(location ->
                        Door.builder()
                                .withLocation(location)
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
                                .build())
                .collect(Collectors.toMap(Wheel::getLocation, wheel -> wheel));
    }
}
