package com.raptordev.raptor.mixin.mixins.accessor;

import com.raptordev.raptor.api.util.font.FontUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.RaptorClient;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin {

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreenInject(int mouseX, int mouseY, float partialTicks, CallbackInfo ci, Color c) {
        FontUtil.drawStringWithShadow(false ,"RaptorClient" + RaptorClient.MODVER, 2, 2, new RCColor(c));
    }
}
