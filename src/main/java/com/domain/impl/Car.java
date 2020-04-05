package com.domain.impl;

import com.constants.Location;
import com.constants.NTransmission;
import com.domain.*;
import com.exception.CarException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import static com.constants.Constants.*;
import static com.constants.Event.*;
import static com.constants.NTransmission.NEUTRAL;
import static com.constants.VehicleLight.DAYTIME_RUNNING;
import static com.helper.Utils.*;
import static java.lang.String.format;

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

    private static final int SPEED_DELTA = 10;
    private static final int GAS_DELTA = 10;
    private static final int BRAKE_DELTA = 10;

    BiPredicate<ControlTransmission, ControlEngine> canSpeedUp =
            (transmission, engine) -> transmission.getTransmission() == NEUTRAL &&
                    engine.isStarted();

    public Car (){
        power = false;
        wheels = new HashMap<>();
        body = new Body();
        tripComputer = new TripComputer(this);
        //body.subscribe(DOOR_OPEN, tripComputer);
    }

    //==================================================

    @Override
    public void insertKey(){
        power = true;
        TripComputer.printOnDashboard(MSG_SEPARATOR1);
        TripComputer.printOnDashboard(MSG_GREETING);
        TripComputer.printOnDashboard(MSG_SEPARATOR1);
        TripComputer.printOnDashboard(MSG_INSERT_KEY);
        checkReadiness();
    }

    @Override
    public void removeKey(){
        power = false;
        System.out.println("Game over!");
    }

    @Override
    public void turnOnKey(){
        try {
            engine.startOn();
            TripComputer.printOnDashboard(MSG_ENGINE_START);
            body.switchOnLight(DAYTIME_RUNNING);
            TripComputer.printOnDashboard(MSG_LIGHT_SWITCH_ON, DAYTIME_RUNNING.getValue());
        } catch (CarException e){
            criticalStopCar();
        }
    }

    @Override
    public void turnBackKey(){
        if (speed != 0)
            TripComputer.printOnDashboard(ERROR, "you are trying to switch off engine while it is moving!");
        engine.startOff();
        TripComputer.printOnDashboard(MSG_ENGINE_STOP);
        body.switchOffLight();
        TripComputer.printOnDashboard(MSG_ALL_LIGHT_SWITCH_OFF);
    }

    @Override
    public void pressGas(int time) {
        if (gas < 100)
            gas += GAS_DELTA;
        changeEngineOnPressGas(time);
    }

    @Override
    public void releaseGas(int time) {
        if (gas > 0)
            gas -= GAS_DELTA;
        changeEngineOnReleaseGas(time);
    }

    @Override
    public void pressBrake(int time) {
        if (brake < 100)
            brake += BRAKE_DELTA;
        changeEngineOnPressBrake(time);
    }

    @Override
    public void releaseBrake(int time) {
        if (brake > 0)
            brake -= BRAKE_DELTA;
        changeEngineOnReleaseBrake(time);
    }

    @Override
    public void steerLeft() {
        changeAngleForSteerLeft(transmission.getDrive(), wheels);
        if (speed > 0) {
            TripComputer.printOnDashboard(MSG_STEER_LEFT);
            if (getRealAngle(wheels) == 0)
                TripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void steerRight() {
        changeAngleForSteerRight(transmission.getDrive(), wheels);
        if (speed > 0) {
            TripComputer.printOnDashboard(MSG_STEER_RIGHT);
            if (getRealAngle(wheels) == 0)
                TripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void steerStraight() {
        changeAngleForSteerStraight(transmission.getDrive(), wheels);
        if (speed > 0) {
            TripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void upGear() {
        NTransmission nextTransmission = transmission.getNextTransmission();
        if (nextTransmission != transmission.getTransmission()) {
            transmission.setTransmission(nextTransmission);
            TripComputer.printOnDashboard(MSG_TRANSMISSION_CHANGE, nextTransmission.getValue());
        }
    }

    @Override
    public void downGear() {
        NTransmission nextTransmission = transmission.getPrevTransmission(engine.getTorque());
        if (nextTransmission != transmission.getTransmission()) {
            transmission.setTransmission(nextTransmission);
            TripComputer.printOnDashboard(MSG_TRANSMISSION_CHANGE, nextTransmission.getValue());
        }
    }

    @Override
    public void checkReadiness(){
        TripComputer.printOnDashboard(MSG_CHECKING_STATUS);
        if (body.checkLights())
            TripComputer.printOnDashboard(MSG_LIGHT_OK);
        else
            TripComputer.printOnDashboard(MSG_LIGHT_DEFECT);
        switch (engine.checkFuelLevel()) {
            case CRITICAL:
                TripComputer.printOnDashboard(ERROR_FUEL_CRITICAL_LEVEL);
                break;
            case LOW:
                TripComputer.printOnDashboard(WARNING_FUEL_LOW_LEVEL);
                break;
            default:
                TripComputer.printOnDashboard(MSG_FUEL_NORMAL_LEVEL);
        }
    }

    @Override
    public void diagnostic(boolean showAll) {
        if (showAll) {
            printDiagnostic(power, speed, gas, brake, wheels, transmission.getTransmission(), engine);
        } else {
            printDiagnostic(power, speed, transmission.getTransmission(), engine);
        }
    }

    @Override
    public float refuel() {
        float newFuel = engine.refuel();
        TripComputer.printOnDashboard(MSG_REFUEL, newFuel);
        return newFuel;
    }

    @Override
    public float refuel(float fuel) {
        float newFuel = engine.refuel(fuel);
        TripComputer.printOnDashboard(MSG_REFUEL, newFuel);
        return newFuel;
    }

    //==================================================

    private void stopCar(){
        transmission.setTransmission(NEUTRAL);
        stopRotated(wheels);
    }

    public void criticalStopCar(){
        speed = 0;
        gas = 0;
        brake = 0;
        transmission.setTransmission(NEUTRAL);
        stopRotated(wheels);
        TripComputer.printOnDashboard(ERROR_FUEL_CRITICAL_LEVEL);
        TripComputer.printOnDashboard(format(MSG_FUEL_LEVEL, engine.getFuel(), engine.getFuel() * 100 / engine.getMaxFuelLevel()));
    }

    private void changeEngineOnPressGas(int time) {
        try {
            int i = 0;
            while (i < time && engine.isStarted()){
                engine.increaseTorque();
                if (canSpeedUp.test(transmission, engine))
                    TripComputer.printOnDashboard(WARNING_TRANSMISSION_NEUTRAL);
                else if (speed < maxSpeed) {
                    speed += SPEED_DELTA;
                    if (speed == SPEED_DELTA)
                        startRotated(wheels);
                    TripComputer.printOnDashboard(MSG_INCREASING_GAS, speed);
                    if (transmission.getNextTransmission(engine.getTorque()) != transmission.getTransmission())
                        TripComputer.printOnDashboard(WARNING_TRANSMISSION_NEXT);
                } else
                    TripComputer.printOnDashboard(WARNING_MAX_SPEED);
                i++;
            }
        } catch (CarException e){
            criticalStopCar();
        }
    }

    private void changeEngineOnReleaseGas(int time) {
        try {
            int i = 0;
            while (i < time && engine.isStarted()){
                engine.decreaseTorque();
                if (canSpeedUp.test(transmission, engine))
                    TripComputer.printOnDashboard(WARNING_TRANSMISSION_NEUTRAL);
                else if (speed > 0) {
                    speed -= SPEED_DELTA;
                    TripComputer.printOnDashboard(MSG_DECREASING_GAS, speed);
                } else {
                    stopCar();
                    TripComputer.printOnDashboard(MSG_CAR_STOPPED);
                }
                i++;
            }
        } catch (CarException e){
            criticalStopCar();
        }
    }

    private void changeEngineOnPressBrake(int time){
        try {
            int i = 0;
            while (i < time && engine.isStarted()){
                engine.decreaseTorque();
                if (speed > 0) {
                    speed -= SPEED_DELTA;
                    TripComputer.printOnDashboard(MSG_INCREASING_BRAKE, speed);
                } else {
                    stopCar();
                    TripComputer.printOnDashboard(MSG_CAR_STOPPED);
                }
                i++;
            }
        } catch (CarException e){
            criticalStopCar();
        }
    }

    public void changeEngineOnReleaseBrake(int time) {
        try {
            int i = 0;
            while (i < time && engine.isStarted()){
                if (brake > 0 && speed > 0) {
                    speed -= SPEED_DELTA;
                    engine.decreaseTorque();
                }
                TripComputer.printOnDashboard(MSG_DECREASING_BRAKE, speed);
                if (speed == 0){
                    stopCar();
                    TripComputer.printOnDashboard(MSG_CAR_STOPPED);
                }
                i++;
            }
        } catch (CarException e){
            criticalStopCar();
        }
    }
    //==================================================

    public boolean isStarted() {
        return engine.isStarted();
    }

    public void setTripComputer(TripComputer tripComputer) {
        this.tripComputer = tripComputer;
    }

    public TripComputer getTripComputer() {
        return tripComputer;
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

    public ControlTransmission getTransmission() {
        return transmission;
    }

    public ControlEngine getEngine() {
        return engine;
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
            car.body.subscribe(DOOR_OPEN, car.getTripComputer());
            car.body.subscribe(DOOR_CLOSE, car.getTripComputer());
            car.engine.subscribe(CRITICAL_FUEL, car.getTripComputer());
            return car;
        }
    }
}
