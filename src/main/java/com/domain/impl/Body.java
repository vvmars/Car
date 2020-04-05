package com.domain.impl;

import com.constants.Event;
import com.constants.LightStatus;
import com.constants.Location;
import com.constants.VehicleLight;
import com.domain.*;
import com.domain.EventListener;
import java.util.*;
import static com.constants.Constants.WARNING_DOOR_LOCKED_OPEN;
import static com.constants.Constants.MSG_DOOR_OPEN;
import static com.constants.Event.DOOR_OPEN;
import static com.constants.LightStatus.OFF;
import static com.constants.LightStatus.ON;
import static java.lang.String.format;

public class Body implements ControlBody, Subscribe {
    private Map<Location, Door> doors;
    private Map<VehicleLight, CarLight> carLights;

    private Map<Event, List<EventListener>> listeners;

    private Body(Map<Location, Door> doors, Map<VehicleLight, CarLight> carLights){
        this.doors = doors;
        this.carLights = carLights;
        listeners = new EnumMap<>(Event.class);
    }

    //==================================================

    @Override
    public boolean checkLights(){
        return carLights.entrySet().stream()
                .map(e -> {
                    LightStatus state = e.getValue().getStatus();
                    e.getValue().changeStatus();
                    boolean isWork = state != e.getValue().getStatus();
                    e.getValue().changeStatus();
                    return isWork;
                })
                .filter(state -> !state)
                .findFirst()
                .orElse(true);
    }

    @Override
    public void switchOffLight(){
        carLights.entrySet().stream().forEach( light -> light.getValue().setStatus(OFF));
    }

    @Override
    public void switchOnLight(VehicleLight vehicleLight){
        carLights.get(vehicleLight).setStatus(ON);
    }

    @Override
    public void switchOffLight(VehicleLight vehicleLight){
        carLights.get(vehicleLight).setStatus(OFF);
    }

    @Override
    public void lockAllDoors() {
        doors.values().forEach(Door::lockDoor);
    }

    @Override
    public void subscribe(Event event, com.domain.EventListener listener){
        List<EventListener> list = listeners.computeIfAbsent(event, (k) -> new ArrayList<>());
        list.add(listener);
    }
    //--------------------------------------------------

    private void notifyListeners(String msg) {
        listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, msg));
    }

    public void openDoorInside(Location location) {
        Door door = doors.get(location);
        door.unlockDoor();
        door.openDoor();
        notifyListeners(format(MSG_DOOR_OPEN, location.getValue()));
    }

    public void openDoorOutside(Location location) {
        Door door = doors.get(location);
        if (door.isStatusLock()) {
            notifyListeners(format(WARNING_DOOR_LOCKED_OPEN, door.getLocation().getValue(), "locked"));
        } else {
            door.openDoor();
            notifyListeners(format(WARNING_DOOR_LOCKED_OPEN, door.getLocation().getValue(), "unlocked"));
        }
    }

    //*********************************************************

    public Map<VehicleLight, CarLight> getCarLights() {
        return carLights;
    }

    public Map<Location, Door> getDoors() {
        return doors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Body)) return false;
        Body body = (Body) o;
        return doors.equals(body.doors) &&
                carLights.equals(body.carLights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doors, carLights);
    }

    @Override
    public String toString() {
        return "Body{" +
                "doors=" + doors +
                ", carLights=" + carLights +
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
        private Map<Location, Door> doors;
        private Map<VehicleLight, CarLight> carLights;

        public Builder(){
            carLights = new EnumMap<>(VehicleLight.class);
        }

        public Builder withDoors(Map<Location, Door> doors){
            this.doors = doors;
            return this;
        }

        public Builder withCarLight(VehicleLight vehicleLight, CarLight carLight){
            this.carLights.put(vehicleLight, carLight);
            return this;
        }

        public Body build(){
            return new Body(doors, carLights);
        }
    }
}
