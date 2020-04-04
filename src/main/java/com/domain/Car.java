package com.domain;

import com.constants.Location;
import java.util.HashMap;
import java.util.Map;
import static com.constants.Constants.*;
import static com.constants.VehicleLight.DAYTIME_RUNNING;

public class Car implements ControlCar {
    private TripComputer tripComputer;
    private ControlBody body;
    private ControlEngine engine;
    private Transmission transmission;
    private Map<Location, Wheel> wheels;

    //0-100%
    private float gas;
    //0-100%
    private float brake;

    private boolean power;
    //0 - maxSpeed
    private int speed;
    private int maxSpeed;
    private int clearance;
    private int seats;

    public Car (){
        power = false;
        wheels = new HashMap<>();
        tripComputer = new TripComputer(this);
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
            tripComputer.printOnDashboard(MSG_ERROR, "you are trying to switch off engine while it is moving!");
        engine.startOff();
        tripComputer.printOnDashboard(MSG_STOP_ENGINE);
        body.switchOffLight();
        tripComputer.printOnDashboard(MSG_ALL_LIGHT_SWITCH_OFF);
    }

    @Override
    public void pressGas() {
        if (gas < 100) {
            gas++;
            speed++;
            engine.increaseTorque();
            engine.consumeFuel(speed);
            tripComputer.printOnDashboard(MSG_INCREASING_GAS, speed);
        } else
            tripComputer.printOnDashboard(MSG_MAX_SPEED);
    }

    @Override
    public void releaseGas() {
        if (gas > 0) {
            gas--;
            engine.decreaseTorque();
            tripComputer.printOnDashboard(MSG_DECREASING_GAS);
        }
    }

    @Override
    public void pressBrake() {
        if (brake < 100) {
            brake++;
            tripComputer.printOnDashboard(MSG_INCREASING_BRAKE);
        }
    }

    @Override
    public void releaseBrake() {
        if (brake > 0) {
            brake--;
            tripComputer.printOnDashboard(MSG_DECREASING_BRAKE);
        }
    }

    @Override
    public void steerLeft() {

    }

    @Override
    public void steerRight() {

    }

    @Override
    public void steerStraight() {

    }

    @Override
    public void changeGear(int gear) {

    }

    @Override
    public void putCarInDrive() {

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
                tripComputer.printOnDashboard(MSG_FUEL_CRITICAL_LEVEL);
                break;
            case LOW:
                tripComputer.printOnDashboard(MSG_FUEL_LOW_LEVEL);
                break;
            default:
                tripComputer.printOnDashboard(MSG_FUEL_NORMAL_LEVEL);
        }
    }

    @Override
    public void diagnostic() {

    }

    @Override
    public float refuel() {
        return 0;
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

    public Transmission getTransmission() {
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

        void withBody(Body body){
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
