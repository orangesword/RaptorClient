package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;

@Module.Declaration(name = "Sprint", category = Category.Movement)
public class Sprint extends Module {

    private BooleanSetting multiDirection = registerBoolean("Multi Direction", true);

    public void onUpdate() {
        EntityPlayerSP player = mc.player;

        if (player != null) {
            player.setSprinting(shouldSprint(player));
        }
    }

    public boolean shouldSprint(EntityPlayerSP player) {
        return !mc.gameSettings.keyBindSneak.isKeyDown()
                && player.getFoodStats().getFoodLevel() > 6
                && !player.isElytraFlying()
                && !mc.player.capabilities.isFlying
                && checkMovementInput(player);
    }

    private boolean checkMovementInput(EntityPlayerSP player) {
        return multiDirection.getValue() ? (player.moveForward != 0.0f || player.moveStrafing != 0.0f) : player.moveForward > 0.0f;
    }
}