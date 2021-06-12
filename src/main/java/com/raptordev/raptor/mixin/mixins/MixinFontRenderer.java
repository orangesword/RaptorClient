package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.api.util.font.FontUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Hoosiers 12/07/2020
 */

@Mixin(FontRenderer.class)
public class MixinFontRenderer {

    @Redirect(method = "drawStringWithShadow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;FFIZ)I"))
    public int drawCustomFontStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color, boolean dropShadow) {
        ColorMain colorMain = ModuleManager.getModule(ColorMain.class);
        return colorMain.textFont.getValue() ? (int) FontUtil.drawStringWithShadow(true, text, (int) x, (int) y, new RCColor(color)) : fontRenderer.drawString(text, x, y, color, true);
    }
}