package com.domain;

import com.constants.Event;
import com.domain.impl.Car;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.constants.Constants.MSG_FUEL_LEVEL;
import static com.constants.Event.*;
import static java.lang.String.format;

/**
 *
 */
public class TripComputer implements EventListener {
    private static Map<Event, Consumer<String>> handlers;
    private static Consumer<String> notify;
    private static Consumer<String> stopEngine;
    private static Car car;

    static {
        notify = TripComputer::printOnDashboard;
        stopEngine = msg -> {
            car.criticalStopCar();
            printOnDashboard(msg);
            printOnDashboard(format(MSG_FUEL_LEVEL, car.getEngine().getFuel(), car.getEngine().getFuel() * 100 / car.getEngine().getMaxFuelLevel()));
            };

        handlers = new HashMap<>();
        handlers.put(DOOR_OPEN, notify);
        handlers.put(DOOR_CLOSE, notify);
        handlers.put(CRITICAL_FUEL, stopEngine);
    }

    public TripComputer(Car currCar){
        car = currCar;
    }

    public static void printOnDashboard(String msg, Object ... args){
        if (args != null && args.length > 0)
            System.out.println(format(msg, args));
        else
            System.out.println(msg);
    }

    @Override
    public void handleEvent(Event event, String msg) {
        handlers.get(event).accept(msg);
    }
}
