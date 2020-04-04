package com.domain;

import com.constants.Event;
import com.domain.impl.Car;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import static com.constants.Event.*;
import static java.lang.String.format;

/**
 *
 */
public class TripComputer implements EventListener {
    private static Map<Event, Consumer<String>> handlers;
    private static Consumer<String> openDoor = System.out::println;
    private Car car;

    static {
        handlers = new HashMap<>();
        handlers.put(DOOR_OPEN, openDoor);
    }

    public TripComputer(Car car){
        this.car = car;
    }

    public void printOnDashboard(String msg, Object ... args){
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
