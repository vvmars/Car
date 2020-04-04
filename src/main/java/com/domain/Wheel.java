package com.domain;

import com.constants.Location;

public class Wheel implements ControlWheel{
    private Location location;
    private int innerRadius;
    private int outerRadius;
    //0 - maxAngle
    private float angle;
    private boolean rotated;

    private static final int maxAngle = 45;

    //==========================================================
    @Override
    public void increaseAngle(){
        if (angle < maxAngle)
            angle += 500;
    }
    @Override
    public void decreaseAngle(){
        if (angle > 0)
            angle -= 500;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
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

        public Builder withAngle(float angle){
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
