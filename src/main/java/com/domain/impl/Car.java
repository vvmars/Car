package com.domain.impl;

import com.constants.Location;
import com.constants.NTransmission;
import com.domain.*;
import com.exception.CarException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private int gas = 0;
    //0-100%
    private int brake = 0;
    private int clearance;
    private int seats;

    private static final int GAS_DELTA = 10;
    private static final int BRAKE_DELTA = 10;

    BiPredicate<ControlTransmission, ControlEngine> canSpeedUp =
            (transmission, engine) -> transmission.getTransmission() == NEUTRAL &&
                    engine.isStarted();

    public Car (ControlBody body, ControlEngine engine,
                ControlTransmission transmission, Map<Location, Wheel> wheels, int maxSpeed,
                int clearance, int seats){

        this.body = body;
        this.engine = engine;
        this.transmission = transmission;
        this.wheels = wheels;
        this.clearance = clearance;
        this.seats = seats;

        this.tripComputer = new TripComputer(maxSpeed);

        body.subscribe(DOOR_OPEN, tripComputer);
        body.subscribe(DOOR_CLOSE, tripComputer);
    }

    //==================================================

    @Override
    public void insertKey(){
        if (tripComputer.isCarStarted())
            TripComputer.printOnDashboard(ERROR_INSERT_KEY);
        else {
            tripComputer.setCarStarted(true);
            body.lockAllDoors();
            TripComputer.printOnDashboard(MSG_SEPARATOR1);
            TripComputer.printOnDashboard(MSG_GREETING);
            TripComputer.printOnDashboard(MSG_SEPARATOR1);
            TripComputer.printOnDashboard(MSG_INSERT_KEY);
            TripComputer.printOnDashboard(MSG_DOOR_LOCK_ALL);
            checkReadiness();
        }
    }

    @Override
    public void removeKey(){
        if (!tripComputer.isCarStarted())
            TripComputer.printOnDashboard(ERROR_REMOVE_KEY);
        else if (tripComputer.getSpeed() > 0)
            TripComputer.printOnDashboard(ERROR_REMOVE_KEY_MOVING);
        else {
            tripComputer.setCarStarted(false);
            System.out.println(" ****** GAME OVER! ******");
        }
    }

    @Override
    public void turnOnKey(){
        if (!tripComputer.isCarStarted())
            TripComputer.printOnDashboard(ERROR_TURN_KEY);
        else if (engine.isStarted())
            TripComputer.printOnDashboard(ERROR_TURN_KEY_STARTED);
        else
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
        if (!tripComputer.isCarStarted())
            TripComputer.printOnDashboard(ERROR, "you are trying to switch off engine while key isn't here!");
        else if (tripComputer.getSpeed() != 0)
            TripComputer.printOnDashboard(ERROR, "you are trying to switch off engine while it is moving!");
        else {
            engine.startOff();
            TripComputer.printOnDashboard(MSG_ENGINE_STOP);
            body.switchOffLight();
            TripComputer.printOnDashboard(MSG_ALL_LIGHT_SWITCH_OFF);
        }
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
        if (brake > 0) {
            brake -= BRAKE_DELTA;
            changeEngineOnReleaseBrake(time);
        }
    }

    @Override
    public void steerLeft() {
        changeAngleForSteerLeft(transmission.getDrive(), wheels);
        if (tripComputer.getSpeed() > 0) {
            TripComputer.printOnDashboard(MSG_STEER_LEFT);
            if (getRealAngle(wheels) == 0)
                TripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void steerRight() {
        changeAngleForSteerRight(transmission.getDrive(), wheels);
        if (tripComputer.getSpeed() > 0) {
            TripComputer.printOnDashboard(MSG_STEER_RIGHT);
            if (getRealAngle(wheels) == 0)
                TripComputer.printOnDashboard(MSG_STEER_STRAIGHT);
        }
    }

    @Override
    public void steerStraight() {
        changeAngleForSteerStraight(transmission.getDrive(), wheels);
        if (tripComputer.getSpeed() > 0) {
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
            printDiagnostic(tripComputer.isCarStarted(), tripComputer.getSpeed(), gas, brake, wheels, transmission.getTransmission(), engine);
        } else {
            printDiagnostic(tripComputer.isCarStarted(), tripComputer.getSpeed(), transmission.getTransmission(), engine);
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

    @Override
    public void openDoorInside(Location location){
        body.openDoorInside(location);
    }

    @Override
    public void openDoorOutside(Location location){
        body.openDoorOutside(location);
    }

    //==================================================

    private void stopCar(){
        transmission.setTransmission(NEUTRAL);
        stopRotated(wheels);
    }

    public void criticalStopCar(){
        tripComputer.resetSpeed();
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
                else if (tripComputer.getSpeed() < tripComputer.getMaxSpeed()) {
                    if (tripComputer.speedUp() == TripComputer.getSpeedDelta())
                        startRotated(wheels);
                    TripComputer.printOnDashboard(MSG_INCREASING_GAS, tripComputer.getSpeed());
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
            int currSpeed;
            int i = 0;
            while (i < time && engine.isStarted()){
                currSpeed = tripComputer.getSpeed();
                engine.decreaseTorque();
                if (canSpeedUp.test(transmission, engine))
                    TripComputer.printOnDashboard(WARNING_TRANSMISSION_NEUTRAL);
                else if (tripComputer.getSpeed() > 0) {
                    tripComputer.speedDown();
                    TripComputer.printOnDashboard(MSG_DECREASING_GAS, tripComputer.getSpeed());
                } else if (currSpeed != tripComputer.getSpeed() && tripComputer.getSpeed() == 0){
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
            int currSpeed;
            int i = 0;
            while (i < time && engine.isStarted()){
                currSpeed = tripComputer.getSpeed();
                engine.decreaseTorque();
                if (tripComputer.getSpeed() > 0) {
                    tripComputer.speedDown();
                    TripComputer.printOnDashboard(MSG_INCREASING_BRAKE, tripComputer.getSpeed());
                } else if (currSpeed != tripComputer.getSpeed() && tripComputer.getSpeed() == 0){
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
            int currSpeed;
            int i = 0;
            while (i < time && engine.isStarted()){
                currSpeed = tripComputer.getSpeed();
                if (tripComputer.getSpeed() > 0) {
                    tripComputer.speedDown();
                    engine.decreaseTorque();
                    TripComputer.printOnDashboard(MSG_DECREASING_BRAKE, tripComputer.getSpeed());
                }
                if (currSpeed != tripComputer.getSpeed() && tripComputer.getSpeed() == 0){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return clearance == car.clearance &&
                seats == car.seats &&
                tripComputer.equals(car.tripComputer) &&
                body.equals(car.body) &&
                engine.equals(car.engine) &&
                transmission.equals(car.transmission) &&
                wheels.equals(car.wheels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripComputer, body, engine, transmission, wheels, clearance, seats);
    }

    @Override
    public String toString() {
        return "Car{" +
                "tripComputer=" + tripComputer +
                ", body=" + body +
                ", engine=" + engine +
                ", transmission=" + transmission +
                ", wheels=" + wheels +
                ", clearance=" + clearance +
                ", seats=" + seats +
                '}';
    }

    //========================================================
    /**
     * static factory method for builder
     */
    public static Builder builder() {
        return new Car.Builder();
    }

    public static class Builder{
        private ControlBody body;
        private ControlEngine engine;
        private ControlTransmission transmission;
        private Map<Location, Wheel> wheels;
        private int maxSpeed;
        private int clearance;
        private int seats;

        Builder(){
            this.wheels = new HashMap<>();
        }

        public Builder withBody(Body body){
            this.body = body;
            return this;
        }

        public Builder withEngine(Engine engine){
            this.engine = engine;
            return this;
        }

        public Builder withTransmission(Transmission transmission){
            this.transmission = transmission;
            return this;
        }

        public Builder withWheels(Map<Location, Wheel> wheels){
            this.wheels = wheels;
            return this;
        }

        public Builder withMaxSpeed(int maxSpeed){
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder withClearance(int clearance){
            this.clearance = clearance;
            return this;
        }

        public Builder withSeats(int seats){
            this.seats = seats;
            return this;
        }

        public Car build(){
            return new Car(body, engine, transmission,
                    wheels, maxSpeed, clearance, seats);
        }
    }
}
