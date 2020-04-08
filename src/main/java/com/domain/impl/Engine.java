package com.domain.impl;

import com.domain.ControlEngine;
import com.exception.CarException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import static com.constants.Constants.ERROR_FUEL_CRITICAL_LEVEL;
import static com.constants.FuelLevel.CRITICAL;

public abstract class Engine implements ControlEngine {
    final static Logger log = Logger.getLogger(Engine.class);
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

    public Engine (int power, String fileProp) throws CarException {
        started = false;
        this.power = power;
        fuelConsumption = new HashMap<>();
        loadProperties(fileProp);
    }


    private void loadProperties(String fileProp) throws CarException {
        Scanner prop = null;
        try (InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileProp)){

            if (file != null) {
                try {
                    prop = new Scanner(file).useDelimiter("=|\\R");
                    while (prop.hasNextInt())
                        fuelConsumption.put(prop.nextInt(), prop.nextInt());
                }finally {
                    if (prop != null)
                        prop.close();
                }
            } else
                throw new CarException("Error loading prop file " + fileProp);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    //=========================================================

    @Override
    public void startOn() throws CarException {
        if (checkFuelLevel() == CRITICAL) {
            log.error("Not enough fuel");
            throw new CarException(ERROR_FUEL_CRITICAL_LEVEL);
        }
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
            log.error("Not enough fuel");
            started = false;
            torque = 0;
            throw new CarException(ERROR_FUEL_CRITICAL_LEVEL);
        }
    }

    @Override
    public void decreaseTorque() throws CarException {
        if (torque > TORQUE_DELTA)
            torque -= TORQUE_DELTA;
        consumeFuel();
        if (checkFuelLevel() == CRITICAL) {
            log.error("Not enough fuel");
            started = false;
            torque = 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Engine)) return false;
        Engine engine = (Engine) o;
        return started == engine.started &&
                power == engine.power &&
                torque == engine.torque &&
                fuelConsumption.equals(engine.fuelConsumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(started, power, torque, fuelConsumption);
    }
}
