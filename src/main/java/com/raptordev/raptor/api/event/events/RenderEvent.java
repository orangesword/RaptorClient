package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;

public class RenderEvent extends RaptorClientMainEvent {

    private final float partialTicks;

    public RenderEvent(float partialTicks) {
        super();
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}