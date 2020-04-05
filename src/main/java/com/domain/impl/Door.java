package com.domain.impl;

import com.constants.Location;
import java.util.Objects;

public class Door {
    private Location location;
    private boolean statusOpen = false;
    private boolean statusLock = false;

    public Door(Location location){
        this.location = location;
    }

    //==========================================================
    public Location getLocation() {
        return location;
    }

    public boolean isStatusLock() {
        return statusLock;
    }

    public void openDoor() {
        statusOpen = true;
    }

    public void closeDoor() {
        statusOpen = false;
    }

    public void lockDoor() {
        statusLock = true;
    }

    public void unlockDoor() {
        statusLock = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Door)) return false;
        Door door = (Door) o;
        return location == door.location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "Door{" +
                "location=" + location +
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

        public Builder withLocation(Location location){
            this.location = location;
            return this;
        }

        public Door build(){
            return new Door(location);
        }
    }
}
