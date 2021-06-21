package com.raptordev.raptor.client.module.modules.misc;

import java.util.Random;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

@Module.Declaration(category = Category.Misc, name = "AntiAfk", Description = "Moves in order not to get kicked. (May be invisible client-sided)")
public class AntiAfk extends Module{

	public BooleanSetting swing = registerBoolean("Swing", true);
	public BooleanSetting turn = registerBoolean("Turn", true);
	public Random random = new Random();
	
	public void onUpdate() {
		if (mc.playerController.isHittingBlock) {
			return;
		}
		if (mc.player.ticksExisted % 40 == 0 && swing.getValue())
            mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        if (mc.player.ticksExisted % 15 == 0 && turn.getValue())
            mc.player.rotationYaw = random.nextInt(360) - 180;

        if (!(swing.getValue() || turn.getValue()) && mc.player.ticksExisted % 80 == 0) {
            mc.player.jump();
        }
	}
	
}
