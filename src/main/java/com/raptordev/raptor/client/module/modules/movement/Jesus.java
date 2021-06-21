package com.raptordev.raptor.client.module.modules.movement;

import java.util.Arrays;

import com.raptordev.raptor.api.event.Phase;
import com.raptordev.raptor.api.event.events.JesusEvent;
import com.raptordev.raptor.api.event.events.OnUpdateWalkingPlayerEvent;
import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.world.EntityUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.render.Freecam;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

@Module.Declaration(name = "Jesus", category = Category.Movement, Description = "Walk on Water")
public class Jesus extends Module{

    private static final AxisAlignedBB WATER_WALK_AA = new AxisAlignedBB(0.D, 0.D, 0.D, 1.D, 0.99D, 1.D);

    public void onUpdate() {
        if (!ModuleManager.isModuleEnabled(Freecam.class)) {
            if (EntityUtil.isInLiquid() && !mc.player.isSneaking()) {
                mc.player.motionY = 0.1;
                if (mc.player.getRidingEntity() != null && !(mc.player.getRidingEntity() instanceof EntityBoat)) {
                    mc.player.getRidingEntity().motionY = 0.3;
                }
            }
        }
    }

    @EventHandler
    Listener<JesusEvent> onJesusUpdated = new Listener<>(event -> {
        if (mc.player == null && mc.world == null) {
            return;
        }
        if (Jesus.mc.player != null && EntityUtil.checkCollide() && !(Jesus.mc.player.motionY >= (double) 0.1f) && (double) event.getBlockPos().getY() < Jesus.mc.player.posY - (double) 0.05f) {
            if (Jesus.mc.player.getRidingEntity() != null) {
                event.setAlignedBB(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.95f, 1.0));
            } else {
                event.setAlignedBB(Block.FULL_BLOCK_AABB);
            }
            event.cancel();
        }

    });

    @EventHandler
    Listener<PacketEvent.Send> packetEventSendListener = new Listener<>(event -> {
            if (event.getPacket() instanceof CPacketPlayer) {
                if (EntityUtil.isAboveWater(mc.player, true) && !EntityUtil.isInLiquid() && !EntityUtil.isAboveLand(mc.player)) {
                    int ticks = mc.player.ticksExisted % 2;
                    if (ticks == 0) ((CPacketPlayer) event.getPacket()).y += 0.02D;
                }
            }
        }
    );


}
