package com.domain;

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
        fuelConsumption.put(0, 15);
        fuelConsumption.put(10, 12);
        fuelConsumption.put(20, 10);
        fuelConsumption.put(40, 8);
        fuelConsumption.put(80, 6);
        fuelConsumption.put(120, 8);
        fuelConsumption.put(160, 14);
        fuelConsumption.put(200, 18);
    }

    //=========================================================

    @Override
    public void consumeFuel(float speed){
        petrolLevel -= getFuelConsumption(speed)/60;
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
        this.petrolLevel = fuel;
    }

    @Override
    public void refuel(){
        this.petrolLevel = maxPetrolLevel;
    }

    @Override
    public FuelLevel checkFuelLevel(){
        float currFuelLevel = petrolLevel * 100 / maxPetrolLevel;
        return currFuelLevel < 1 ? CRITICAL : currFuelLevel < 5 ? LOW : NORMAL;
    }

    //=========================================================

    public float getPetrolLevel() {
        return petrolLevel;
    }

    public void setPetrolLevel(float petrolLevel) {
        this.petrolLevel = petrolLevel;
    }

    public float getMaxPetrolLevel() {
        return maxPetrolLevel;
    }
}
