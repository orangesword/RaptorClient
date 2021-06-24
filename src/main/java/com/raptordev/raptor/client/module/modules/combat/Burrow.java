package com.raptordev.raptor.client.module.modules.combat;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.player.BurrowUtil;
import com.raptordev.raptor.api.util.world.HoleUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name = "Burrow", category = Category.Combat)
public class Burrow extends Module {

    BooleanSetting rotate = registerBoolean("Rotate", true);
    BooleanSetting smart = registerBoolean("Smart", true);
    DoubleSetting offset = registerDouble("OffSet", 7, -10, 10);
    DoubleSetting smartDistance = registerDouble("SmartDistance", 2.5, 1, 7);
    private BlockPos originalPos;
    private int oldSlot = -1;

    protected void onEnable() {
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            toggle();
            return;
        }
        oldSlot = mc.player.inventory.currentItem;
    }

    public void onUpdate() {
        if (smart.getValue() && HoleUtil.isInHole(mc.player)) {
            mc.world.loadedEntityList.stream()
                    .filter(e -> e instanceof EntityPlayer)
                    .filter(e -> e != mc.player)
                    .forEach(e -> {
                        if (e.getDistance(mc.player) + 0.22f <= smartDistance.getValue())
                            doBurrow();
                    });
        } else
            doBurrow();
    }

    public void doBurrow() {

        // If we don't have obsidian in hotbar toggle and return
        if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            MessageBus.sendClientPrefixMessage("Can't find obsidian in hotbar");
            toggle();
            return;
        }

        // Change to obsidian slot
        BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockObsidian.class));

        // Fake jump
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

        // Place block
        BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);

        // Rubberband
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

        // SwitchBack
        BurrowUtil.switchToSlot(oldSlot);

        // AutoDisable
        toggle();
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

}
