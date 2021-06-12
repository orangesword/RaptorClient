package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

public class PlayerLeaveEvent extends RaptorClientMainEvent {

    private final String name;

    public PlayerLeaveEvent(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}