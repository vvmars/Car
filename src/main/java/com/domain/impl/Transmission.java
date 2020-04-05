package com.domain.impl;

import com.constants.Drive;
import com.constants.NTransmission;
import com.domain.ControlTransmission;

import static com.constants.NTransmission.*;

public class Transmission implements ControlTransmission {
    private boolean automatic;
    private NTransmission transmission;
    private Drive drive;

    public Transmission(boolean automatic, Drive drive){
        this.automatic = automatic;
        this.drive = drive;
        if (automatic)
            transmission = IN_PARK;
        else
            transmission = NEUTRAL;
    }

    //=======================================================================

    @Override
    public Drive getDrive() {
        return drive;
    }

    @Override
    public NTransmission getTransmission() {
        return transmission;
    }

    @Override
    public void setTransmission(NTransmission transmission) {
        this.transmission = transmission;
    }

    @Override
    public NTransmission getNextTransmission(int torque){
        if (transmission != REVERSE && transmission != SIXTH && torque > 2500){
            return getNextTransmission();
        } else return transmission;
    }

    @Override
    public NTransmission getNextTransmission(){
        NTransmission nextTransmission = transmission;
        if (transmission == REVERSE)
            nextTransmission = NEUTRAL;
        else if (transmission == NEUTRAL)
            nextTransmission = FIRST;
        else if (transmission == FIRST)
            nextTransmission = SECOND;
        else if (transmission == SECOND)
            nextTransmission = THIRD;
        else if (transmission == THIRD)
            nextTransmission = FOURTH;
        else if (transmission == FOURTH)
            nextTransmission = FIFTH;
        return nextTransmission;
    }

    @Override
    public NTransmission getPrevTransmission(int torque){
        if (transmission != REVERSE && torque < 1000){
            return getPrevTransmission();
        } else return transmission;
    };

    @Override
    public NTransmission getPrevTransmission(){
        NTransmission prevTransmission = transmission;
        if (transmission == NEUTRAL)
            prevTransmission = REVERSE;
        else if (transmission == FIRST)
            prevTransmission = NEUTRAL;
        else if (transmission == SECOND)
            prevTransmission = FIRST;
        else if (transmission == THIRD)
            prevTransmission = SECOND;
        else if (transmission == FOURTH)
            prevTransmission = THIRD;
        else if (transmission == FIFTH)
            prevTransmission = FOURTH;
        else if (transmission == SIXTH)
            prevTransmission = FIFTH;
        return prevTransmission;
    }
    //=======================================================================

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
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

    public static class Builder{
        private boolean automatic;
        private Drive drive;

        public Builder withAutomatic(boolean automatic){
            this.automatic = automatic;
            return this;
        }

        public Builder withDrive(Drive drive){
            this.drive = drive;
            return this;
        }

        public Transmission build(){
            return new Transmission(automatic, drive);
        }
    }
}
