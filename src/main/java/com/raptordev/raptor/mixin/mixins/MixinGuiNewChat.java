package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.misc.ChatModifier;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(int left, int top, int right, int bottom, int color) {
        ChatModifier chatModifier = ModuleManager.getModule(ChatModifier.class);

        if (!chatModifier.isEnabled() || !chatModifier.clearBkg.getValue()) {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }
}