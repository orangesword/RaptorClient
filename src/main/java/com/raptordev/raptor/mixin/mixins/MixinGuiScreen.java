package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.render.ShulkerViewer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo callbackInfo) {
        ShulkerViewer shulkerViewer = ModuleManager.getModule(ShulkerViewer.class);

        if (shulkerViewer.isEnabled() && stack.getItem() instanceof ItemShulkerBox) {
            if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
                if (stack.getTagCompound().getCompoundTag("BlockEntityTag").hasKey("Items", 9)) {
                    callbackInfo.cancel();
                    shulkerViewer.renderShulkerPreview(stack, x + 6, y - 33, 162, 66);
                }
            }
        }
    }
}