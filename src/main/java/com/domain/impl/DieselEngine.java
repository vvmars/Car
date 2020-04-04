package com.domain.impl;

import com.constants.FuelLevel;

import java.util.Comparator;
import java.util.Map;

import static com.constants.FuelLevel.*;

public class DieselEngine extends Engine {
    private float dieselLevel;
    private final float maxDieselLevel;

    public DieselEngine(float maxDieselLevel, int power){
        super(power);
        this.maxDieselLevel = maxDieselLevel;
        fuelConsumption.put(0, 0);
        fuelConsumption.put(500, 9);
        fuelConsumption.put(1000, 8);
        fuelConsumption.put(1300, 7);
        fuelConsumption.put(1500, 6);
        fuelConsumption.put(1800, 5);
        fuelConsumption.put(2500, 8);
        fuelConsumption.put(3000, 10);
        fuelConsumption.put(3500, 12);
    }

    //=========================================================

    @Override
    public void consumeFuel(){
        dieselLevel -= getFuelConsumption()/60;
    }

    /**
     * return fuel in L/h
     */
    @Override
    public float getFuelConsumption(){
        return fuelConsumption.entrySet().stream()
                .filter(e -> e.getKey() < torque)
                .max(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(0) * power / 150f;
    }

    @Override
    public void refuel(float fuel){
        this.dieselLevel = fuel;
    }

    @Override
    public void refuel(){
        this.dieselLevel = maxDieselLevel;
    }

    @Override
    public float getFuel(){
        return dieselLevel;
    }

    @Override
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = dieselLevel * 100 / maxDieselLevel;
        return currFuelLevel < 1 ? CRITICAL : currFuelLevel < 4 ? LOW : NORMAL;
    }
    //=========================================================

    public void setDieselLevel(float dieselLevel) {
        this.dieselLevel = dieselLevel;
    }
}
