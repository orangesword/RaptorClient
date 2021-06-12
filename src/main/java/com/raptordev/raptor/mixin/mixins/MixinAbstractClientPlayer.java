package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.api.util.render.CapeUtil;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.render.CapesModule;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        UUID uuid = getPlayerInfo().getGameProfile().getId();
        CapesModule capesModule = ModuleManager.getModule(CapesModule.class);

        if (capesModule.isEnabled() && CapeUtil.hasCape(uuid)) {
            if (capesModule.capeMode.getValue().equalsIgnoreCase("Black")) {
                callbackInfoReturnable.setReturnValue(new ResourceLocation("gamesense:capeblack.png"));
            } else {
                callbackInfoReturnable.setReturnValue(new ResourceLocation("gamesense:capewhite.png"));
            }
        }
    }
}