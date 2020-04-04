package com.constants;

public enum LightStatus {
    ON("on"){
        public LightStatus getOppositeState(){
            return OFF;
        }
    },
    OFF("off"){
        public LightStatus getOppositeState(){
            return ON;
        }
    },
    FUSED("fused"){
        public LightStatus getOppositeState(){
            return FUSED;
        }
    };

    private String lightStatus;
    LightStatus(String lightStatus){
        this.lightStatus = lightStatus;
    }

    public abstract LightStatus getOppositeState();

    public String getValue() {
        return lightStatus;
    }
}
