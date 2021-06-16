package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.api.event.events.EntityCollisionEvent;
import com.raptordev.raptor.client.RaptorClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void velocity(Entity entityIn, CallbackInfo ci) {
        EntityCollisionEvent event = new EntityCollisionEvent();
        RaptorClient.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

}