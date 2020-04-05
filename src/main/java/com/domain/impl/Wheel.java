package com.domain.impl;

import com.constants.Location;
import com.domain.ControlWheel;

import java.util.Objects;

public class Wheel implements ControlWheel {
    private Location location;
    private int innerRadius;
    private int outerRadius;
    //0 - MAX_ANGLE
    private int angle = 0;
    private boolean rotated = false;
    private static final int ANGLE_DELTA = 5;
    private static final int MAX_ANGLE = 45;

    public Wheel(Location location, int innerRadius, int outerRadius){
        this.location = location;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
    }

    //==========================================================
    @Override
    public void increaseAngle(){
        if (angle < MAX_ANGLE)
            angle += ANGLE_DELTA;
    }

    @Override
    public void decreaseAngle(){
        if (angle > -MAX_ANGLE)
            angle -= ANGLE_DELTA;
    }

    @Override
    public void resetAngle(){
        angle = 0;
    }

    //==========================================================

    public Location getLocation() {
        return location;
    }

    public int getAngle() {
        return angle;
    }

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wheel)) return false;
        Wheel wheel = (Wheel) o;
        return innerRadius == wheel.innerRadius &&
                outerRadius == wheel.outerRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerRadius, outerRadius);
    }

    @Override
    public String toString() {
        return "Wheel{" +
                "innerRadius=" + innerRadius +
                ", outerRadius=" + outerRadius +
                '}';
    }

    //==========================================================
    /**
     * static factory method for builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private Location location;
        private int innerRadius;
        private int outerRadius;

        public Builder withLocation(Location location){
            this.location = location;
            return this;
        }

        public Builder withInnerRadius(int innerRadius){
            this.innerRadius = innerRadius;
            return this;
        }

        public Builder withOuterRadius(int outerRadius){
            this.outerRadius = outerRadius;
            return this;
        }

        public Wheel build(){
            return new Wheel(location, innerRadius, outerRadius);
        }
    }
}
