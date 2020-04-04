package com.domain.impl;

import com.constants.Location;
import com.constants.NTransmission;
import com.domain.*;

import java.util.HashMap;
import java.util.Map;
import static com.constants.Constants.*;
import static com.constants.NTransmission.NEUTRAL;
import static com.constants.VehicleLight.DAYTIME_RUNNING;
import static com.helper.Utils.*;

public class Car implements ControlCar {
    private TripComputer tripComputer;
    private ControlBody body;
    private ControlEngine engine;
    private ControlTransmission transmission;
    private Map<Location, Wheel> wheels;

    //0-100%
    private int gas;
    //0-100%
    private int brake;

    private boolean power;
    //0 - maxSpeed
    private int speed;
    private int maxSpeed;
    private int clearance;
    private int seats;

    private static final int SPEAD_DELTA = 10;
    private static final int GAS_DELTA = 10;
    private static final int BRAKE_DELTA = 10;

    public Car (){
        power = false;
        wheels = new HashMap<>();
        tripComputer = new TripComputer(this);
        //body.subscribe(DOOR_OPEN, tripComputer);
    }

    //==================================================

    @Override
    public void insertKey(){
        power = true;
        tripComputer.printOnDashboard(MSG_SEPARATOR1);
        tripComputer.printOnDashboard(MSG_GREETING);
        tripComputer.printOnDashboard(MSG_SEPARATOR1);
        tripComputer.printOnDashboard(MSG_INSERT_KEY);
        checkReadiness();
    }

    @Override
    public void removeKey(){
        power = false;
        System.out.println("Game over!");
    }

    @Override
    public void turnOnKey(){
        engine.startOn();
        tripComputer.printOnDashboard(MSG_START_ENGINE);
        body.switchOnLight(DAYTIME_RUNNING);
        tripComputer.printOnDashboard(MSG_LIGHT_SWITCH_ON, DAYTIME_RUNNING.getValue());
    }

    @Override
    public void turnBackKey(){
        if (speed != 0)
            tripComputer.printOnDashboard(ERROR, "you are trying to switch off engine while it is moving!");
        engine.startOff();
        tripComputer.printOnDashboard(MSG_STOP_ENGINE);
        body.switchOffLight();
        tripComputer.printOnDashboard(MSG_ALL_LIGHT_SWITCH_OFF);
    }

    @Override
    public void pressGas(int time) {
        for (int i = 0; i < time; i++)
            pressGas();
    }

    @Override
    public void releaseGas(int time) {
        for (int i = 0; i < time; i++)
            releaseGas();
    }

    @Override
    public void pressBrake(int time) {
        for (int i = 0; i < time; i++)
            pressBrake();
    }

    @Override
    public void releaseBrake(int time) {
        for (int i = 0; i < time; i++)
            releaseBrake();
    }

    @Override
    public void steerLeft() {
        if (speed > 0) {
            changeAngleForSteerLeft(transmission.getDrive(), wheels);
            tripComputer.printOnDashboard(MSG_STEER_LEFT);
        }
        if (getRealAngle(wheels) == 0)
            tripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
    }

    @Override
    public void steerRight() {
        if (speed > 0) {
            changeAngleForSteerRight(transmission.getDrive(), wheels);
            tripComputer.printOnDashboard(MSG_STEER_RIGHT);
        }
        if (getRealAngle(wheels) == 0)
            tripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
    }

    @Override
    public void steerStraight() {
        if (speed > 0) {
            changeAngleForSteerStraight(transmission.getDrive(), wheels);
            tripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void upGear() {
        NTransmission nextTransmission = transmission.getNextTransmission();
        if (nextTransmission != transmission.getTransmission()) {
            transmission.setTransmission(nextTransmission);
            tripComputer.printOnDashboard(MSG_TRANSMISSION_CHANGE, nextTransmission.getValue());
        }
    }

    @Override
    public void downGear() {
        NTransmission nextTransmission = transmission.getPrevTransmission(engine.getTorque());
        if (nextTransmission != transmission.getTransmission()) {
            transmission.setTransmission(nextTransmission);
            tripComputer.printOnDashboard(MSG_TRANSMISSION_CHANGE, nextTransmission.getValue());
        }
    }

    @Override
    public void checkReadiness(){
        tripComputer.printOnDashboard(MSG_CHECKING_STATUS);
        if (body.checkLights())
            tripComputer.printOnDashboard(MSG_LIGHT_OK);
        else
            tripComputer.printOnDashboard(MSG_LIGHT_DEFECT);
        switch (engine.checkFuelLevel()) {
            case CRITICAL:
                tripComputer.printOnDashboard(ERROR_FUEL_CRITICAL_LEVEL);
                break;
            case LOW:
                tripComputer.printOnDashboard(WARNING_FUEL_LOW_LEVEL);
                break;
            default:
                tripComputer.printOnDashboard(MSG_FUEL_NORMAL_LEVEL);
        }
    }

    @Override
    public void diagnostic() {
        printDiagnostic(tripComputer, power, speed, gas, brake, wheels, transmission.getTransmission(), engine);
    }

    @Override
    public float refuel() {
        return 0;
    }

    //==================================================

    private void stopCar(){
        transmission.setTransmission(NEUTRAL);
        stopRotated(wheels);
    }

    private void pressGas() {
        if (gas < 100) {
            gas += GAS_DELTA;
            engine.increaseTorque();
            engine.consumeFuel();
            if (transmission.getTransmission() == NEUTRAL)
                tripComputer.printOnDashboard(WARNING_TRANSMISSION_NEUTRAL);
            else if (speed < maxSpeed) {
                speed += SPEAD_DELTA;
                if (speed == SPEAD_DELTA)
                    startRotated(wheels);
                tripComputer.printOnDashboard(MSG_INCREASING_GAS, speed);
                if (transmission.getNextTransmission(engine.getTorque()) != transmission.getTransmission())
                    tripComputer.printOnDashboard(WARNING_TRANSMISSION_NEXT);
            } else
                tripComputer.printOnDashboard(WARNING_MAX_SPEED);
        } else
            tripComputer.printOnDashboard(WARNING_MAX_SPEED);
    }

    private void releaseGas() {
        if (gas > 0) {
            gas -= GAS_DELTA;
            engine.decreaseTorque();
            engine.consumeFuel();
            if (transmission.getTransmission() == NEUTRAL)
                tripComputer.printOnDashboard(WARNING_TRANSMISSION_NEUTRAL);
            else if (speed > 0) {
                speed -= SPEAD_DELTA;
                tripComputer.printOnDashboard(MSG_DECREASING_GAS, speed);
            } else {
                stopCar();
                tripComputer.printOnDashboard(MSG_CAR_STOPPED);
            }
        } else
            tripComputer.printOnDashboard(MSG_CAR_STOPPED);
    }

    private void pressBrake(){
        if (brake < 100) {
            brake += BRAKE_DELTA;
            engine.decreaseTorque();
            engine.consumeFuel();
            if (speed > 0) {
                speed -= SPEAD_DELTA;
                tripComputer.printOnDashboard(MSG_INCREASING_BRAKE, speed);
            } else {
                stopCar();
                tripComputer.printOnDashboard(MSG_CAR_STOPPED);
            }
        }
    }

    public void releaseBrake() {
        if (brake > 0) {
            brake -= BRAKE_DELTA;
            if (brake > 0 && speed > 0) {
                speed -= SPEAD_DELTA;
                engine.decreaseTorque();
            }
            engine.consumeFuel();
            tripComputer.printOnDashboard(MSG_DECREASING_BRAKE, speed);
            if (speed == 0){
                stopCar();
                tripComputer.printOnDashboard(MSG_CAR_STOPPED);
            }
        }
    }
    //==================================================

    public void setTripComputer(TripComputer tripComputer) {
        this.tripComputer = tripComputer;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }

    public boolean isPower() {
        return power;
    }

    public ControlTransmission getTransmission() {
        return transmission;
    }

    public void setWheels(Map<Location, Wheel> wheels) {
        this.wheels = wheels;
    }

    public Map<Location, Wheel> getWheels() {
        return wheels;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    //========================================================
    /**
     * static factory method for builder
     */
    public static Builder builder() {
        return new Car.Builder();
    }

    public static class Builder{
        private Car car;

        Builder(){
            car = new Car();
        }

        public void withBody(Body body){
            car.setBody(body);
        }

        public Body.Builder withBody(){
            return Body.builder(this);
        }

        public Builder withEngine(Engine engine){
            car.setEngine(engine);
            return this;
        }

        public void withTransmission(Transmission transmission){
            car.setTransmission(transmission);
        }

        public Transmission.Builder withTransmission(){
            return Transmission.builder(this);
        }

        public void withWheel(Location location, Wheel wheel){
            car.getWheels().put(location, wheel);
        }

        public Builder withWheels(Map<Location, Wheel> wheels){
            car.setWheels(wheels);
            return this;
        }

        public Wheel.Builder withWheels(){
            return Wheel.builder(this);
        }

        public Builder withMaxSpeed(int maxSpeed){
            car.setMaxSpeed(maxSpeed);
            return this;
        }

        public Builder withClearance(int clearance){
            car.setClearance(clearance);
            return this;
        }

        public Car build(){
            return car;
        }
    }
}
