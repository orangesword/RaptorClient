package com.raptordev.raptor.client.module.modules.movement;

import org.lwjgl.input.Keyboard;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.client.gui.GuiChat;

@Module.Declaration(name = "GuiMove", category = Category.Movement)
public class GuiMove extends Module {


	BooleanSetting chatgui = registerBoolean("ChatGui", false);
	
    public void onUpdate() {
        if (mc.currentScreen != null) {
        	if (chatgui.getValue()) {
				
			}
            if (!(mc.currentScreen instanceof GuiChat)) {
                if (Keyboard.isKeyDown(200)) {
                    mc.player.rotationPitch -= 5;
                }
                if (Keyboard.isKeyDown(208)) {
                    mc.player.rotationPitch += 5;
                }
                if (Keyboard.isKeyDown(205)) {
                    mc.player.rotationYaw += 5;
                }
                if (Keyboard.isKeyDown(203)) {
                    mc.player.rotationYaw -= 5;
                }
                if (mc.player.rotationPitch > 90) {
                    mc.player.rotationPitch = 90;
                }
                if (mc.player.rotationPitch < -90) {
                    mc.player.rotationPitch = -90;
                }
            }
        }
    }

}
