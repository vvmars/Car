package com.helper;

import com.constants.Location;
import com.domain.impl.Wheel;
import com.service.CarFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static com.constants.Drive.FRONT;
import static com.constants.Location.FRONT_LEFT;
import static com.constants.Location.FRONT_RIGHT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class UtilsTest {
    private Map<Location, Wheel> wheels;
    @BeforeEach
    void setUp() {
        wheels = CarFactory.wheelsBuilder(14, 16);
    }

    @Test
    void startRotated() {
        Utils.startRotated(wheels);
        wheels.values().forEach(wheel ->
                assertThat(wheel.isRotated(), is(equalTo(true))));
    }

    @Test
    void stopRotated() {
        Utils.stopRotated(wheels);
        wheels.values().forEach(wheel ->
                assertThat(wheel.isRotated(), is(equalTo(false))));
    }

    @Test
    void changeAngleForSteerLeft() {
        for (int i = 5; i < 11; i += 5) {
            Utils.changeAngleForSteerLeft(FRONT, wheels);
            int finalI = i;
            wheels.entrySet().stream()
                    .filter(entry -> entry.getValue().getLocation() == FRONT_LEFT ||
                            entry.getValue().getLocation() == FRONT_RIGHT)
                    .forEach(entry ->
                            assertThat(entry.getValue().getAngle(), is(equalTo(finalI))));
        }
    }

    @Test
    void changeAngleForSteerRight() {
        for (int i = -5; i > -11; i -= 5) {
            Utils.changeAngleForSteerRight(FRONT, wheels);
            int finalI = i;
            wheels.entrySet().stream()
                    .filter(entry -> entry.getValue().getLocation() == FRONT_LEFT ||
                            entry.getValue().getLocation() == FRONT_RIGHT)
                    .forEach(entry ->
                            assertThat(entry.getValue().getAngle(), is(equalTo(finalI))));
        }
    }

    @Test
    void changeAngleForSteerStraight() {
        Utils.changeAngleForSteerRight(FRONT, wheels);
        Utils.changeAngleForSteerStraight(FRONT, wheels);

        wheels.entrySet().stream()
                .filter(entry -> entry.getValue().getLocation() == FRONT_LEFT ||
                        entry.getValue().getLocation() == FRONT_RIGHT)
                .forEach(entry ->
                        assertThat(entry.getValue().getAngle(), is(equalTo(0))));
    }

    @Test
    void getRealAngle() {
        Utils.changeAngleForSteerRight(FRONT, wheels);
        assertThat(Utils.getRealAngle(wheels), is(equalTo(-5)));
    }
}