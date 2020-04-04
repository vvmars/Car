package com.domain;

import com.constants.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.constants.FuelLevel.CRITICAL;

public abstract class Engine implements ControlEngine{
    private boolean started;
    //HP
    protected final int power;
    //0 - MAX_TORQUE
    private int torque;
    private static final float MAX_TORQUE = 5000;
    protected final Map<Integer, Integer> fuelConsumption;
    //private heater;
    //private oilLevel;

    private Map<Event, List<EventListener>> listeners = new HashMap<>();

    public Engine (int power){
        started = false;
        this.power = power;
        fuelConsumption = new HashMap<>();
    }

    //=========================================================

    @Override
    public boolean startOn(){
        if (checkFuelLevel() == CRITICAL)
            return false;
        else return started = true;
    }

    @Override
    public boolean startOff(){
        return started = false;
    }

    @Override
    public void increaseTorque(){
        if (torque < MAX_TORQUE)
            torque += 500;
    }

    @Override
    public void decreaseTorque(){
        if (torque > 0)
            torque -= 500;
    }

    //=========================================================

    public void subscribe(Event event, EventListener listener){
        List<EventListener> list = listeners.computeIfAbsent(event, (k) -> new ArrayList<>());
        list.add(listener);
    }

    //=========================================================

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getPower() {
        return power;
    }

    public int getTorque() {
        return torque;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    public Map<Integer, Integer> getFuelConsumption() {
        return fuelConsumption;
    }
}
