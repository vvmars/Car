package com.domain.impl;

import com.constants.LightStatus;

public class CarLight {
    private LightStatus status;

    public CarLight(LightStatus status){
        this.status = status;
    }

    public LightStatus changeStatus() {
        status = status.getOppositeState();
        return status;
    }

    public LightStatus getStatus() {
        return status;
    }

    public void setStatus(LightStatus status) {
        this.status = status;
    }
}
