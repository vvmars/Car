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

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isStatusOpen() {
        return statusOpen;
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
        if (o == null || getClass() != o.getClass()) return false;
        Door door = (Door) o;
        return location == door.location &&
                statusOpen == door.statusOpen &&
                statusLock == door.statusLock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, statusOpen, statusLock);
    }

    @Override
    public String toString() {
        return "Door{" +
                "doorType=" + location +
                ", statusOpen=" + statusOpen +
                ", statusLock=" + statusLock +
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
