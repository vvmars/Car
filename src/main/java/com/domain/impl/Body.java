package com.domain.impl;

import com.constants.Event;
import com.constants.LightStatus;
import com.constants.Location;
import com.constants.VehicleLight;
import com.domain.*;
import com.domain.EventListener;
import java.util.*;
import static com.constants.Constants.MSG_DOOR_LOCKED_OPEN;
import static com.constants.Constants.MSG_DOOR_OPEN;
import static com.constants.Event.DOOR_OPEN;
import static com.constants.LightStatus.OFF;
import static com.constants.LightStatus.ON;
import static java.lang.String.format;

public class Body implements ControlBody, Subscribe {
    private Map<Location, Door> doors;
    private Map<VehicleLight, CarLight> carLights;

    private Map<Event, List<EventListener>> listeners;

    public Body(){
        doors = new EnumMap<>(Location.class);
        carLights = new EnumMap<>(VehicleLight.class);
        listeners = new EnumMap<>(Event.class);
    }

    public Body(Map<Location, Door> doors, Map<VehicleLight, CarLight> carLights){
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
    public void subscribe(Event event, com.domain.EventListener listener){
        List<EventListener> list = listeners.computeIfAbsent(event, (k) -> new ArrayList<>());
        list.add(listener);
    }
    //--------------------------------------------------

    public void openDoorInside(Location location) {
        Door door = doors.get(location);
        door.unlockDoor();
        door.openDoor();
        notifyListeners(location);
    }

    private void notifyListeners(Location location) {
        listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, format(MSG_DOOR_OPEN, location)));
    }

    public void openDoorOutside(Location location) {
        Door door = doors.get(location);
        if (door.isStatusLock()) {
            listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, format(MSG_DOOR_LOCKED_OPEN, door.getLocation(), door.isStatusLock())));
        } else {
            door.openDoor();
            notifyListeners(location);
        }
    }

    //*********************************************************

    public void addDoors(Location location, Door door) {
        doors.put(location, door);
    }

    public void setDoors(Map<Location, Door> doors) {
        this.doors = doors;
    }

    public void addCarLight(VehicleLight vehicleLight, CarLight carLight) {
        carLights.put(vehicleLight, carLight);
    }

    public Map<VehicleLight, CarLight> getCarLights() {
        return carLights;
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
