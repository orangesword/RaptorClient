package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

public class PlayerJoinEvent extends RaptorClientMainEvent {

    private final String name;

    public PlayerJoinEvent(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}