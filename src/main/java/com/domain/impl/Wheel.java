package com.domain.impl;

import com.constants.Location;
import com.domain.ControlWheel;

public class Wheel implements ControlWheel {
    private Location location;
    private int innerRadius;
    private int outerRadius;
    //0 - maxAngle
    private int angle;
    private boolean rotated;
    private static final int ANGLE_DELTA = 5;
    private static final int maxAngle = 45;

    //==========================================================
    @Override
    public void increaseAngle(){
        if (angle < maxAngle)
            angle += ANGLE_DELTA;
    }

    @Override
    public void decreaseAngle(){
        if (angle > -maxAngle)
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

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(int innerRadius) {
        this.innerRadius = innerRadius;
    }

    public int getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(int outerRadius) {
        this.outerRadius = outerRadius;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }
//==========================================================
    /**
     * static factory method for builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Car.Builder carBuilder) {
        return new Builder(carBuilder);
    }

    public static class Builder{
        private Wheel wheel;
        private Car.Builder carBuilder;

        Builder(){
            wheel = new Wheel();
        }

        Builder(Car.Builder carBuilder){
            this();
            this.carBuilder = carBuilder;
        }

        public Builder withLocation(Location location){
            wheel.location = location;
            return this;
        }

        public Builder withInnerRadius(int innerRadius){
            wheel.innerRadius = innerRadius;
            return this;
        }

        public Builder withOuterRadius(int outerRadius){
            wheel.outerRadius = outerRadius;
            return this;
        }

        public Builder withAngle(int angle){
            wheel.angle = angle;
            return this;
        }

        public Builder withRotated(boolean rotated){
            wheel.rotated = rotated;
            return this;
        }

        public Wheel build(){
            return wheel;
        }

        public Car.Builder done(){
            carBuilder.withWheel(wheel.location, wheel);
            return carBuilder;
        }
    }
}
