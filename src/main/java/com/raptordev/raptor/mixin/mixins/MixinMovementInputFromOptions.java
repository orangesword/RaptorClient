package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.movement.GuiMove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MovementInputFromOptions.class, priority = 10000)
public abstract class MixinMovementInputFromOptions extends MovementInput {

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        int keyCode = keyBinding.getKeyCode();

        if (keyCode > 0 && keyCode < Keyboard.KEYBOARD_SIZE) {
            GuiMove module = ModuleManager.getModule(GuiMove.class);

            if (module.isEnabled()
                && Minecraft.getMinecraft().currentScreen != null
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
                return Keyboard.isKeyDown(keyCode);
            }
        }

        return keyBinding.isKeyDown();
    }
}