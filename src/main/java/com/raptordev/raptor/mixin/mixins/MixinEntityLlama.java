package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.api.event.events.EntitySteerEvent;
import com.raptordev.raptor.client.RaptorClient;
import net.minecraft.entity.passive.EntityLlama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLlama.class)
public class MixinEntityLlama {

    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)

    public void canBeSteered(CallbackInfoReturnable<Boolean> cir)
    {
        EntitySteerEvent event = new EntitySteerEvent();
        RaptorClient.EVENT_BUS.post(event);

        if (event.isCancelled())
        {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }

}
