package com.domain;

public abstract  class CarProxy implements ControlCar{
    private ControlCar car;

    public CarProxy(ControlCar car){
        this.car = car;
    }

    public void insertKey(){
        car.insertKey();
    }
    public void removeKey(){
        car.removeKey();
    }
    public void turnOnKey(){
        car.turnOnKey();
    }
    public void turnBackKey(){
        car.turnBackKey();
    }

 /*    public void pressGas(int time){
        if (!car.isStarted())
            System.out.println("The car is not started yet");
        else
            for (int i = 0; i < time; i++)
                car.pressGas(time);
    }
   public void releaseGas(int time){
        if (!car.isStarted())
            System.out.println("The car is not started yet");
        else
            for (int i = 0; i < time; i++)
                car.releaseGas(time);
    }
    public void pressBrake(int time){
        if (!car.isStarted())
            System.out.println("The car is not started yet");
        else
            for (int i = 0; i < time; i++)
                car.pressBrake(time);
    }
    public void releaseBrake(int time){
        if (!car.isStarted())
            System.out.println("The car is not started yet");
        else
            for (int i = 0; i < time; i++)
                car.releaseBrake(time);
    }*/

    public void steerLeft(){
        car.steerLeft();
    }
    public void steerRight(){
        car.steerRight();
    }
    public void steerStraight(){
        car.steerStraight();
    }

    public void upGear(){
        car.upGear();
    }
    public void downGear(){
        car.downGear();
    }

    //PutCarInPark
    //void putCarInDrive();
    //PutCarInReverse
    public void checkReadiness(){
        car.checkReadiness();
    }

    public void diagnostic(boolean showAll){
        car.diagnostic(showAll);
    }

    public float refuel(){
        return car.refuel();
    }
}
