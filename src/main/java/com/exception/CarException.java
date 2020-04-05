package com.exception;

import static java.lang.String.format;

public class CarException extends Exception {
    String msg;
    public CarException(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public String toString() {
        return format("CarException[ %s]", msg);
    }
}
