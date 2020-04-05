package com.domain.impl;

import com.constants.FuelLevel;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.constants.FuelLevel.*;

public class PetrolEngine extends Engine{
    private float petrolLevel;
    private final float maxPetrolLevel;

    public PetrolEngine(float maxPetrolLevel, int power){
        super(power);
        this.maxPetrolLevel = maxPetrolLevel;
        fuelConsumption.put(0, 0);
        fuelConsumption.put(10, 12);
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
    public float refuel(float fuel){
        petrolLevel = Math.min(maxPetrolLevel - petrolLevel, petrolLevel + fuel);
        return petrolLevel;
    }

    @Override
    public float refuel(){
        petrolLevel = maxPetrolLevel;
        return petrolLevel;
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

    @Override
    public float getMaxFuelLevel() {
        return maxPetrolLevel;
    }
    //=========================================================

    public void setPetrolLevel(float petrolLevel) {
        this.petrolLevel = petrolLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetrolEngine)) return false;
        if (!super.equals(o)) return false;
        PetrolEngine that = (PetrolEngine) o;
        return Float.compare(that.petrolLevel, petrolLevel) == 0 &&
                Float.compare(that.maxPetrolLevel, maxPetrolLevel) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), petrolLevel, maxPetrolLevel);
    }

    @Override
    public String toString() {
        return "PetrolEngine{" +
                "petrolLevel=" + petrolLevel +
                ", maxPetrolLevel=" + maxPetrolLevel +
                '}';
    }
}
