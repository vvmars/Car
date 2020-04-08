package com.domain.impl;

import com.constants.FuelLevel;
import com.exception.CarException;
import org.apache.log4j.Logger;
import static java.lang.String.format;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import static com.constants.FuelLevel.*;

public class ElectroEngine extends Engine {
    final static Logger log = Logger.getLogger(ElectroEngine.class);
    private float chargeLevel;
    private final float maxChargeLevel;
    private static final String propFile = "electro.properties";

    public ElectroEngine(float maxChargeLevel, int power) throws CarException {
        super(power, propFile);
        this.maxChargeLevel = maxChargeLevel;
    }

    //=========================================================
    @Override
    public void consumeFuel(){
        chargeLevel -= getFuelConsumption()/60;
        log.info(format("Consume fuel - %s", chargeLevel));
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
    public float refuel(float fuel){
        chargeLevel = Math.min(maxChargeLevel - chargeLevel, chargeLevel + fuel);
        return chargeLevel;
    }

    @Override
    public float refuel(){
        chargeLevel = maxChargeLevel;
        return chargeLevel;
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

    @Override
    public float getMaxFuelLevel() {
        return maxChargeLevel;
    }
    //=========================================================

    public void setChargeLevel(float chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectroEngine)) return false;
        if (!super.equals(o)) return false;
        ElectroEngine that = (ElectroEngine) o;
        return Float.compare(that.chargeLevel, chargeLevel) == 0 &&
                Float.compare(that.maxChargeLevel, maxChargeLevel) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chargeLevel, maxChargeLevel);
    }

    @Override
    public String toString() {
        return "ElectroEngine{" +
                "chargeLevel=" + chargeLevel +
                ", maxChargeLevel=" + maxChargeLevel +
                '}';
    }
}

