package com.domain;

import com.constants.Drive;
import com.constants.NTransmission;

public class Transmission {
    private boolean automatic;
    private NTransmission transmission;
    private Drive drive;

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public NTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(NTransmission transmission) {
        this.transmission = transmission;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    //=======================================================================
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
        private Transmission transmission;
        private Car.Builder carBuilder;

        Builder(){
            transmission = new Transmission();
        }

        Builder(Car.Builder carBuilder){
            this();
            this.carBuilder = carBuilder;
        }

        public Builder withAutomatic(boolean automatic){
            transmission.automatic = automatic;
            return this;
        }

        public Builder withTransmission(NTransmission transmission){
            this.transmission.transmission = transmission;
            return this;
        }

        public Builder withDrive(Drive drive){
            transmission.drive = drive;
            return this;
        }

        public Transmission build(){
            return transmission;
        }

        public Car.Builder done(){
            carBuilder.withTransmission(transmission);
            return carBuilder;
        }
    }
}
