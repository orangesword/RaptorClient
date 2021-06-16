package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.api.event.events.PlayerJumpEvent;
import com.raptordev.raptor.api.event.events.PlayerTravelEvent;
import com.raptordev.raptor.api.event.events.WaterPushEvent;
import com.raptordev.raptor.client.RaptorClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinAbstractEnity{

    @Shadow
    public abstract String getName();

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player.getName() == this.getName()) {
            RaptorClient.EVENT_BUS.post(new PlayerJumpEvent());
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo info)
    {
        PlayerTravelEvent l_Event = new PlayerTravelEvent(strafe, vertical, forward);
        RaptorClient.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
        {
            move(MoverType.SELF, motionX, motionY, motionZ);
            info.cancel();
        }
    }

    @Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
    private void onPushedByWater(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        WaterPushEvent event = new WaterPushEvent();
        RaptorClient.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }



}