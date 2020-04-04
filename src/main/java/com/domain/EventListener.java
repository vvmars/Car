package com.domain;

import com.constants.Event;

public interface EventListener {
    void handleEvent(Event event, String msg);
}
