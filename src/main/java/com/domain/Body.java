package com.domain;

import com.constants.Event;
import com.constants.LightStatus;
import com.constants.Location;
import com.constants.VehicleLight;

import java.util.*;
import static com.constants.Event.DOOR_OPEN;
import static com.constants.LightStatus.OFF;
import static com.constants.LightStatus.ON;
import static java.lang.String.format;

public class Body implements ControlBody{
    private Map<Location, Door> doors;
    private Map<VehicleLight, CarLight> carLights;

    private Map<Event, List<EventListener>> listeners;

    private static final String MSG_DOOR_OPEN = "%s door was opened";
    private static final String MSG_DOOR_LOCKED_OPEN = "Passenger's is trying to open %s which is %s";

    public Body(){
        doors = new EnumMap<>(Location.class);
        carLights = new EnumMap<>(VehicleLight.class);
        listeners = new EnumMap<>(Event.class);
    }

    //==================================================

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

    public void switchOffLight(){
        carLights.entrySet().stream().forEach( light -> light.getValue().setStatus(OFF));
    }

    public void switchOnLight(VehicleLight vehicleLight){
        carLights.get(vehicleLight).setStatus(ON);
    }

    public void switchOffLight(VehicleLight vehicleLight){
        carLights.get(vehicleLight).setStatus(OFF);
    }

    //--------------------------------------------------

    public void openDoorInside(Location location) {
        Door door = doors.get(location);
        door.unlockDoor();
        door.openDoor();
        listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, format(MSG_DOOR_OPEN, location)));
    }

    public void openDoorOutside(Location location) {
        Door door = doors.get(location);
        if (door.isStatusLock()) {
            listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, format(MSG_DOOR_LOCKED_OPEN, door.getLocation(), door.isStatusLock())));
            //System.out.println(format("Passenger's is trying to open %s which is %s", door.getDoorType(), door.isStatusLock()));
        } else {
            door.openDoor();
            listeners.get(DOOR_OPEN).forEach(listener -> listener.handleEvent(DOOR_OPEN, format(MSG_DOOR_OPEN, location)));
        }
    }

    @Override
    public void subscribe(Event event, EventListener listener){
        List<EventListener> list = listeners.computeIfAbsent(event, (k) -> new ArrayList<>());
        list.add(listener);
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

    public static Builder builder(Car.Builder carBuilder) {
        return new Builder(carBuilder);
    }

    public static class Builder{
        private Body body;
        private Car.Builder carBuilder;

        Builder(){
            body = new Body();
        }

        Builder(Car.Builder carBuilder){
            this();
            this.carBuilder = carBuilder;
        }

        public void withDoor(Location location, Door door){
            body.addDoors(location, door);
        }

        public Door.Builder withDoors(){
            return Door.builder(this);
        }

        public Builder withDoors(Map<Location, Door> doors){
            body.setDoors(doors);
            return this;
        }

        public Builder withCarLight(VehicleLight vehicleLight, CarLight carLight){
            body.carLights.put(vehicleLight, carLight);
            return this;
        }

        public Body build(){
            return body;
        }

        public Car.Builder done(){
            carBuilder.withBody(body);
            return carBuilder;
        }
    }
}
