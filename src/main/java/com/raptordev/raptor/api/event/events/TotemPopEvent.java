package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends RaptorClientMainEvent {

    private final Entity entity;

    public TotemPopEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}