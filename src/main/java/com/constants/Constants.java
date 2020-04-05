package com.constants;

public class Constants {
    //==================================================
    public static final String MSG_SEPARATOR1 = "**********************************************";
    public static final String MSG_SEPARATOR2 = "----------------------------------------------";
    public static final String MSG_GREETING = "Welcome aboard, captain!";
    public static final String ERROR = "Error, %s";
    public static final String WARNING = "Warning, %s";

    public static final String MSG_INSERT_KEY = "The power On";
    public static final String ERROR_INSERT_KEY = "You are trying to insert key but it is in place";
    public static final String ERROR_REMOVE_KEY = "You are trying to remove key but it isn't here";
    public static final String ERROR_REMOVE_KEY_MOVING = "You are trying to remove key but the cas is moving";
    public static final String ERROR_TURN_KEY = "You are trying to turn key but it isn't here";
    public static final String ERROR_TURN_KEY_STARTED = "You are trying to turn key but engine was already started";
    public static final String MSG_CAR_STOPPED = "The car's stopped";

    public static final String MSG_CHECKING_STATUS = "checking status ...";
    public static final String MSG_LIGHT_OK = "* light - OK";
    public static final String MSG_LIGHT_DEFECT = "* light - DEFECT";

    public static final String MSG_ENGINE_START = "The engine is running";
    public static final String MSG_ENGINE_STOP = "The engine's stopped";
    public static final String ERROR_ENGINE_FUEL_CRITICAL_LEVEL = "!!! Error, engine's stopped: critical level of fuel";

    public static final String MSG_FUEL_NORMAL_LEVEL = "The level of fuel - normal";
    public static final String WARNING_FUEL_LOW_LEVEL = "* warning, low level of fuel";
    public static final String ERROR_FUEL_CRITICAL_LEVEL = "!!! Error, critical level of fuel";
    public static final String MSG_FUEL_LEVEL = "The current level of fuel - %.2f /%.2f %%/";
    public static final String MSG_REFUEL = "The car was refuelled to - %.2f";

    public static final String MSG_LIGHT_SWITCH_ON = "%s - ON";
    public static final String MSG_LIGHT_SWITCH_OFF = "%s - OFF";
    public static final String MSG_ALL_LIGHT_SWITCH_OFF = "All lights - OFF";

    public static final String MSG_INCREASING_GAS = "increasing gas ... # speed - %s";
    public static final String MSG_DECREASING_GAS = "decreasing gas ... # speed - %s";
    public static final String MSG_INCREASING_BRAKE = "increasing brake ... # speed - %s";
    public static final String MSG_DECREASING_BRAKE = "decreasing brake ... # speed - %s";
    public static final String WARNING_MAX_SPEED = "* warning, speed - maximum";

    public static final String WARNING_TRANSMISSION_NEUTRAL = "* warning, you have not set transmission";
    public static final String WARNING_TRANSMISSION_NEXT = "* warning, you may need to set next transmission";
    public static final String MSG_TRANSMISSION_CHANGE = "Changing transmission to - %s";

    public static final String MSG_STEER_LEFT = "The car is turning left";
    public static final String MSG_STEER_RIGHT = "The car is turning right";
    public static final String MSG_STEER_STRAIGHT = "The car is moving forward";

    public static final String MSG_PRINT_STATE          = " ******** CURRENT STATE ******** ";
    public static final String MSG_STATE_POWER          = "| - power ................. - %s";
    public static final String MSG_STATE_SPEED          = "| - speed ................. - %s";
    public static final String MSG_STATE_GAS            = "| --- gas ................. - %d";
    public static final String MSG_STATE_BRAKE          = "| --- brake ............... - %d";
    public static final String MSG_STATE_WHEELS         = "| - wheels ..................... ";
    public static final String MSG_STATE_WHEEL          = "| --- %s .... ";
    public static final String MSG_STATE_WHEEL_ROTATED  = "| ------ rotated .......... - %s";
    public static final String MSG_STATE_WHEEL_ANGLE    = "| ------ angle   .......... - %d";
    public static final String MSG_STATE_TRANSMISSION   = "| - transmission  ......... - %s";
    public static final String MSG_STATE_ENGINE_STARTED = "| - engine  ............... - %s";
    public static final String MSG_STATE_ENGINE_TORQUE  = "| --- torque  ............. - %d";
    public static final String MSG_STATE_ENGINE_FUEL    = "| --- fuel  ............... %.2f";
    public static final String MSG_STATE_ENGINE_FUEL_CONSUMPTION = "| --- fuel consumption .... %.2f";

    public static final String MSG_DOOR_OPEN = " * warning, %s door was opened";
    public static final String MSG_DOOR_LOCK_ALL = "All doors are locked";
    public static final String WARNING_DOOR_LOCKED_OPEN = " * warning, passenger's trying to open %s door which is %s";
}
