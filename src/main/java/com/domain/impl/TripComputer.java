package com.domain.impl;

import com.constants.Event;
import com.domain.EventListener;
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
    private static Consumer<String> notify;

    private boolean carStarted = false;
    private int speed = 0;
    private int maxSpeed;
    private static final int SPEED_DELTA = 10;

    static {
        notify = TripComputer::printOnDashboard;
        handlers = new HashMap<>();
        handlers.put(DOOR_OPEN, notify);
        handlers.put(DOOR_CLOSE, notify);
    }

    TripComputer(int maxSpeed){
        this.maxSpeed = maxSpeed;
    }

    //==================================================

    public static void printOnDashboard(String msg, Object ... args){
        if (args != null && args.length > 0)
            System.out.println(format(msg, args));
        else
            System.out.println(msg);
    }

    int speedUp(){
        return speed += SPEED_DELTA;
    }

    void speedDown(){
        speed -= SPEED_DELTA;
    }

    void resetSpeed() {
        speed = 0;
    }

    @Override
    public void handleEvent(Event event, String msg) {
        handlers.get(event).accept(msg);
    }

    //==================================================


    boolean isCarStarted() {
        return carStarted;
    }

    void setCarStarted(boolean carStarted) {
        this.carStarted = carStarted;
    }

    int getSpeed() {
        return speed;
    }

    int getMaxSpeed() {
        return maxSpeed;
    }

    static int getSpeedDelta() {
        return SPEED_DELTA;
    }

    @Override
    public String toString() {
        return "TripComputer{" +
                "carStarted=" + carStarted +
                ", speed=" + speed +
                ", maxSpeed=" + maxSpeed +
                '}';
    }
}
