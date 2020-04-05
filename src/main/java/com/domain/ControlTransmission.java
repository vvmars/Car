package com.domain;

import com.constants.Drive;
import com.constants.NTransmission;

public interface ControlTransmission {
    Drive getDrive();
    NTransmission getTransmission();
    boolean isAutomatic();
    void setTransmission(NTransmission transmission);
    NTransmission getNextTransmission();
    NTransmission getNextTransmission(int torque);
    NTransmission getPrevTransmission();
    NTransmission getPrevTransmission(int torque);
}
