package com.domain.impl;

import com.constants.LightStatus;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarLight)) return false;
        CarLight carLight = (CarLight) o;
        return status == carLight.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "CarLight{" +
                "status=" + status +
                '}';
    }
}
