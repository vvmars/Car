package com.domain.impl;

import com.exception.CarException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class PetrolEngineTest {

    private PetrolEngine engine;// = new PetrolEngine(60, 120);

    @BeforeEach
    void setUp() {
        engine = new PetrolEngine(60, 120);
        engine.setPetrolLevel(2);
    }

    @Test
    void startOnWithException(){
        engine.setPetrolLevel(0.5f);
        Exception e = Assertions.assertThrows(CarException.class, () -> engine.startOn());
        assertThat(e.getMessage(), is(equalTo("!!! Error, critical level of fuel")));
    }

    @Test
    void startOn() throws CarException {
        engine.startOn();
        assertThat(engine.getTorque(), is(equalTo(500)));
        assertThat(engine.isStarted(), is(equalTo(true)));
    }

    @Test
    void getFuelConsumption() throws CarException {
        assertThat(engine.getFuelConsumption(), is(equalTo(0f)));
        engine.torque = 1000;
        assertThat(engine.getFuelConsumption(), is(equalTo(9.6f)));
        engine.torque = 1501;
        assertThat(engine.getFuelConsumption(), is(equalTo(4.8f)));
    }

    @Test
    void increaseTorque() throws CarException {
        engine.startOn();
        engine.increaseTorque();
        assertThat(engine.getTorque(), is(equalTo(1000)));
    }

    @Test
    void increaseTorqueWithException() throws CarException {
        engine.setPetrolLevel(0.8f);
        engine.startOn();
        Exception e = Assertions.assertThrows(CarException.class, () -> engine.increaseTorque());
        assertThat(e.getMessage(), is(equalTo("!!! Error, critical level of fuel")));
        assertThat(engine.getTorque(), is(equalTo(0)));
        assertThat(engine.isStarted(), is(equalTo(false)));
    }

    @Test
    void decreaseTorque() throws CarException {
        engine.startOn();
        engine.decreaseTorque();
        assertThat(engine.getTorque(), is(equalTo(500)));
    }

    @Test
    void decreaseTorqueWithException() throws CarException {
        engine.setPetrolLevel(0.8f);
        engine.startOn();
        Exception e = Assertions.assertThrows(CarException.class, () -> engine.decreaseTorque());
        assertThat(e.getMessage(), is(equalTo("!!! Error, critical level of fuel")));
        assertThat(engine.getTorque(), is(equalTo(0)));
        assertThat(engine.isStarted(), is(equalTo(false)));
    }
}