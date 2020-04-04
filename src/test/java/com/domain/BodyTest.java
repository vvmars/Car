package com.domain;

import com.domain.impl.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.constants.LightStatus.*;
import static com.constants.VehicleLight.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class BodyTest {
    private Body body;

    @BeforeEach
    void setUp() {
        body = new Body();
        body.addCarLight(HEAD, new CarLight(OFF));
        body.addCarLight(HAZARD, new CarLight(OFF));
        body.addCarLight(BRAKE, new CarLight(OFF));
        body.addCarLight(FOG, new CarLight(OFF));
        body.addCarLight(DAYTIME_RUNNING, new CarLight(OFF));
        body.addCarLight(LEFT_SIGNAL, new CarLight(OFF));
        body.addCarLight(RIGHT_SIGNAL, new CarLight(OFF));
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
}