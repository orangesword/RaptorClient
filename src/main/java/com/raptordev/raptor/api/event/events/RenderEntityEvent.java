package com.raptordev.raptor.api.event.events;

import com.raptordev.raptor.api.event.RaptorClientMainEvent;
import net.minecraft.entity.Entity;

/**
 * @author Hoosiers
 * @since 12/31/2020
 */

public class RenderEntityEvent extends RaptorClientMainEvent {

    private final Entity entity;
    private final Type type;

    public RenderEntityEvent(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public enum Type {
        TEXTURE,
        COLOR
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Type getType() {
        return this.type;
    }

    public static class Head extends RenderEntityEvent {
        public Head(Entity entity, Type type) {
            super(entity, type);
        }
    }

    public static class Return extends RenderEntityEvent {
        public Return(Entity entity, Type type) {
            super(entity, type);
        }
    }
}