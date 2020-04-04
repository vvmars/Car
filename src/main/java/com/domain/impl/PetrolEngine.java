package com.domain.impl;

import com.constants.FuelLevel;

import java.util.Comparator;
import java.util.Map;

import static com.constants.FuelLevel.*;

public class PetrolEngine extends Engine{
    private float petrolLevel;
    private final float maxPetrolLevel;

    public PetrolEngine(float maxPetrolLevel, int power){
        super(power);
        this.maxPetrolLevel = maxPetrolLevel;
        fuelConsumption.put(0, 0);
        fuelConsumption.put(500, 12);
        fuelConsumption.put(1000, 10);
        fuelConsumption.put(1300, 8);
        fuelConsumption.put(1500, 6);
        fuelConsumption.put(1800, 8);
        fuelConsumption.put(2500, 14);
        fuelConsumption.put(3000, 16);
        fuelConsumption.put(3500, 18);
    }

    //=========================================================

    @Override
    public void consumeFuel(){
        petrolLevel -= getFuelConsumption()/60;
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
        this.petrolLevel = fuel;
    }

    @Override
    public void refuel(){
        this.petrolLevel = maxPetrolLevel;
    }

    @Override
    public float getFuel(){
        return petrolLevel;
    }

    @Override
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = petrolLevel * 100 / maxPetrolLevel;
        return currFuelLevel < 1 ? CRITICAL : currFuelLevel < 5 ? LOW : NORMAL;
    }

    //=========================================================

    public void setPetrolLevel(float petrolLevel) {
        this.petrolLevel = petrolLevel;
    }
}
