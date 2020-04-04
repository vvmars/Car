package com.domain;

import com.constants.Event;

public interface Subscribe {
    void subscribe(Event event, EventListener listener);
}
