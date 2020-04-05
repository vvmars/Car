package com.domain.impl;

import com.constants.Event;
import com.domain.ControlEngine;
import com.domain.EventListener;
import com.exception.CarException;
import java.util.*;
import static com.constants.Constants.ERROR_FUEL_CRITICAL_LEVEL;
import static com.constants.FuelLevel.CRITICAL;

public abstract class Engine implements ControlEngine {
    private boolean started;
    //HP
    protected final int power;
    //0 - MAX_TORQUE
    protected int torque;
    private static final float TORQUE_MAX = 3500;
    private static final float TORQUE_DELTA = 500;
    protected final Map<Integer, Integer> fuelConsumption;
    //private heater;
    //private oilLevel;

    private Map<Event, List<EventListener>> listeners;

    public Engine (int power){
        started = false;
        this.power = power;
        fuelConsumption = new HashMap<>();
        listeners = new EnumMap<>(Event.class);
    }

    //=========================================================

    @Override
    public void startOn() throws CarException {
        if (checkFuelLevel() == CRITICAL)
            throw new CarException(ERROR_FUEL_CRITICAL_LEVEL);
        else {
            increaseTorque();
            started = true;
        }
    }

    @Override
    public boolean startOff(){
        return started = false;
    }

    @Override
    public void increaseTorque() throws CarException {
        if (torque < TORQUE_MAX)
            torque += TORQUE_DELTA;
        consumeFuel();
        if (checkFuelLevel() == CRITICAL) {
            started = false;
            torque = 0;
//            listeners.get(CRITICAL_FUEL)
//                    .forEach(listener -> listener.handleEvent(CRITICAL_FUEL, ERROR_FUEL_CRITICAL_LEVEL));
            throw new CarException(ERROR_FUEL_CRITICAL_LEVEL);
        }
    }

    @Override
    public void decreaseTorque() throws CarException {
        if (torque > TORQUE_DELTA)
            torque -= TORQUE_DELTA;
        consumeFuel();
        if (checkFuelLevel() == CRITICAL) {
            started = false;
            torque = 0;
//            listeners.get(CRITICAL_FUEL)
//                    .forEach(listener -> listener.handleEvent(CRITICAL_FUEL, ERROR_FUEL_CRITICAL_LEVEL));
            throw new CarException(ERROR_FUEL_CRITICAL_LEVEL);
        }
    }

    @Override
    public int getTorque() {
        return torque;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    //=========================================================

    public void subscribe(Event event, EventListener listener){
        List<EventListener> list = listeners.computeIfAbsent(event, (k) -> new ArrayList<>());
        list.add(listener);
    }

    //=========================================================

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getPower() {
        return power;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    /*public Map<Integer, Integer> getFuelConsumption() {
        return fuelConsumption;
    }*/
}
