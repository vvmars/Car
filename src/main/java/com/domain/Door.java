package com.domain;

import com.constants.Location;
import java.util.Objects;

public class Door {
    private Location location;
    private boolean statusOpen;
    private boolean statusLock;

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

    public static Builder builder(Body.Builder bodyBuilder) {
        return new Builder(bodyBuilder);
    }

    public static class Builder{
        private Door door;
        private Body.Builder bodyBuilder;

        Builder(){
            door = new Door();
        }

        Builder(Body.Builder bodyBuilder){
            this();
            this.bodyBuilder = bodyBuilder;
        }

        public Builder withLocation(Location location){
            door.location = location;
            return this;
        }

        public Builder withStatusOpen(boolean statusOpen){
            door.statusOpen = statusOpen;
            return this;
        }

        public Builder withStatusLock(boolean statusLock){
            door.statusLock = statusLock;
            return this;
        }

        public Door build(){
            return door;
        }

        public Body.Builder done(){
            bodyBuilder.withDoor(door.location, door);
            return bodyBuilder;
        }
    }
}
