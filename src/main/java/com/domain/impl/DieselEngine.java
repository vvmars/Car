package com.domain.impl;

import com.constants.FuelLevel;
import com.exception.CarException;
import org.apache.log4j.Logger;
import static java.lang.String.format;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.constants.FuelLevel.*;

public class DieselEngine extends Engine {
    final static Logger log = Logger.getLogger(DieselEngine.class);
    private float dieselLevel;
    private final float maxDieselLevel;
    private static final String propFile = "diesel.properties";

    public DieselEngine(float maxDieselLevel, int power) throws CarException {
        super(power, propFile);
        this.maxDieselLevel = maxDieselLevel;
    }

    //=========================================================

    @Override
    public void consumeFuel(){
        dieselLevel -= getFuelConsumption()/60;
        log.info(format("Consume fuel - %s", dieselLevel));
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
        dieselLevel = Math.min(maxDieselLevel - dieselLevel, dieselLevel + fuel);
        return dieselLevel;
    }

    @Override
    public float refuel(){
        dieselLevel = maxDieselLevel;
        return dieselLevel;
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

    @Override
    public float getMaxFuelLevel() {
        return maxDieselLevel;
    }

    //=========================================================

    public void setDieselLevel(float dieselLevel) {
        this.dieselLevel = dieselLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DieselEngine)) return false;
        if (!super.equals(o)) return false;
        DieselEngine that = (DieselEngine) o;
        return Float.compare(that.dieselLevel, dieselLevel) == 0 &&
                Float.compare(that.maxDieselLevel, maxDieselLevel) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dieselLevel, maxDieselLevel);
    }

    @Override
    public String toString() {
        return "DieselEngine{" +
                "dieselLevel=" + dieselLevel +
                ", maxDieselLevel=" + maxDieselLevel +
                '}';
    }
}
