package com.domain.impl;

import com.constants.FuelLevel;

import java.util.Comparator;
import java.util.Map;

import static com.constants.FuelLevel.*;

public class ElectroEngine extends Engine {
    private float chargeLevel;
    private final float maxChargeLevel;

    public ElectroEngine(float maxChargeLevel, int power){
        super(power);
        this.maxChargeLevel = maxChargeLevel;
        fuelConsumption.put(0, 0);
        fuelConsumption.put(500, 1800);
        fuelConsumption.put(1000, 1500);
        fuelConsumption.put(1300, 1200);
        fuelConsumption.put(1500, 900);
        fuelConsumption.put(1800, 800);
        fuelConsumption.put(2500, 1200);
        fuelConsumption.put(3000, 1500);
        fuelConsumption.put(3500, 2000);
    }

    //=========================================================
    @Override
    public void consumeFuel(){
        chargeLevel -= getFuelConsumption()/60;
    }

    /**
     * return fuel in mA/h
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
        this.chargeLevel = fuel;
    }

    @Override
    public void refuel(){
        this.chargeLevel = maxChargeLevel;
    }

    @Override
    public float getFuel(){
        return chargeLevel;
    }

    @Override
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = chargeLevel * 100 / maxChargeLevel;
        return currFuelLevel < 2 ? CRITICAL : currFuelLevel < 7 ? LOW : NORMAL;
    }

    //=========================================================

    public void setChargeLevel(float chargeLevel) {
        this.chargeLevel = chargeLevel;
    }
}

