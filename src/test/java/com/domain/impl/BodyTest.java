package com.domain.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.constants.LightStatus.*;
import static com.constants.VehicleLight.*;
import static com.service.CarFactory.doorsBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class BodyTest {
    private Body body;

    @BeforeEach
    void setUp() {
        body = Body.builder()
                .withDoors(doorsBuilder())
                .withCarLight(HEAD, new CarLight(OFF))
                .withCarLight(HAZARD, new CarLight(OFF))
                .withCarLight(BRAKE, new CarLight(OFF))
                .withCarLight(DAYTIME_RUNNING, new CarLight(OFF))
                .withCarLight(LEFT_SIGNAL, new CarLight(OFF))
                .withCarLight(RIGHT_SIGNAL, new CarLight(OFF))
                .build();
    }

    @Test
    void checkLightsWithAllOff() {
        assertThat(body.checkLights(), is(equalTo(true)));
    }

    @Test
    void checkDifferLights() {
        body.getCarLights().get(BRAKE).changeStatus();
        body.getCarLights().get(RIGHT_SIGNAL).changeStatus();
        assertThat(body.checkLights(), is(equalTo(true)));
    }

    @Test
    void checkLightsWithFused() {
        body.getCarLights().get(BRAKE).setStatus(FUSED);
        assertThat(body.checkLights(), is(equalTo(false)));
    }

    @Test
    void checkSwitchOffAllLight(){
        body.switchOffLight();
        body.getCarLights().values().forEach(carLight ->
                assertThat(carLight.getStatus(), is(equalTo(OFF))));
    }

    @Test
    void checkSwitchOnLight(){
        body.switchOnLight(BRAKE);
        assertThat(body.getCarLights().get(BRAKE).getStatus(), is(equalTo(ON)));
    }

    @Test
    void checkSwitchOffLight(){
        body.switchOffLight(BRAKE);
        assertThat(body.getCarLights().get(BRAKE).getStatus(), is(equalTo(OFF)));
    }

    @Test
    void checklockAllDoors(){
        body.lockAllDoors();
        body.getDoors().values().forEach(door -> assertThat(door.isStatusLock(), is(equalTo(true))));
    }

}