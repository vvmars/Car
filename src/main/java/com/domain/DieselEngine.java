package com.domain;

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
        fuelConsumption.put(0, 10);
        fuelConsumption.put(10, 9);
        fuelConsumption.put(20, 8);
        fuelConsumption.put(40, 7);
        fuelConsumption.put(80, 6);
        fuelConsumption.put(120, 5);
        fuelConsumption.put(160, 8);
        fuelConsumption.put(200, 10);
    }

    //=========================================================

    @Override
    public void consumeFuel(float speed){
        dieselLevel -= getFuelConsumption(speed)/3600;
    }

    /**
     * return fuel in L/h
     */
    @Override
    public float getFuelConsumption(float speed){
        return fuelConsumption.entrySet().stream()
                .filter(e -> e.getKey() < speed)
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
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = dieselLevel * 100 / maxDieselLevel;
        return currFuelLevel < 1 ? CRITICAL : currFuelLevel < 4 ? LOW : NORMAL;
    }
    //=========================================================

    public float getDieselLevel() {
        return dieselLevel;
    }

    public void setDieselLevel(float dieselLevel) {
        this.dieselLevel = dieselLevel;
    }

    public float getMaxDieselLevel() {
        return maxDieselLevel;
    }
}
