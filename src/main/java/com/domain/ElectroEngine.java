package com.domain;

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
        fuelConsumption.put(0, 1500);
        fuelConsumption.put(10, 1800);
        fuelConsumption.put(20, 1500);
        fuelConsumption.put(40, 1200);
        fuelConsumption.put(80, 900);
        fuelConsumption.put(120, 800);
        fuelConsumption.put(160, 1200);
        fuelConsumption.put(200, 1500);
    }

    //=========================================================
    @Override
    public void consumeFuel(float speed){
        chargeLevel -= getFuelConsumption(speed)/3600;
    }

    /**
     * return fuel in mA/h
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
        this.chargeLevel = fuel;
    }

    @Override
    public void refuel(){
        this.chargeLevel = maxChargeLevel;
    }

    @Override
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = chargeLevel * 100 / maxChargeLevel;
        return currFuelLevel < 2 ? CRITICAL : currFuelLevel < 7 ? LOW : NORMAL;
    }

    //=========================================================

    public float getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(float chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    public float getMaxChargeLevel() {
        return maxChargeLevel;
    }
}

