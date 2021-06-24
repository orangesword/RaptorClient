package com.raptordev.raptor.client.module.modules.combat;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.api.util.player.InventoryUtil;
import com.raptordev.raptor.api.util.player.PlacementUtil;
import com.raptordev.raptor.api.util.player.PlayerUtil;
import com.raptordev.raptor.api.util.world.BlockUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.api.util.misc.Offsets;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

@Module.Declaration(name = "Surround", category = Category.Combat, Description = "Surrounds your feet with obsidian to prevent end crystal attacks")
public class Surround extends Module {

    ModeSetting jumpMode = registerMode("Jump", Arrays.asList("Continue", "Pause", "Disable"), "Continue");
    ModeSetting offsetMode = registerMode("Pattern", Arrays.asList("Normal", "Anti City"), "Normal");
    IntegerSetting delayTicks = registerInteger("Tick Delay", 3, 0, 10);
    IntegerSetting blocksPerTick = registerInteger("Blocks Per Tick", 4, 0, 8);
    BooleanSetting rotate = registerBoolean("Rotate", true);
    BooleanSetting centerPlayer = registerBoolean("Center Player", true);
    BooleanSetting sneakOnly = registerBoolean("Sneak Only", false);
    BooleanSetting disableNoBlock = registerBoolean("Disable No Obby", true);
    BooleanSetting offhandObby = registerBoolean("Offhand Obby", false);

    private final Timer delayTimer = new Timer();
    private Vec3d centeredBlock = Vec3d.ZERO;

    private int oldSlot = -1;
    private int offsetSteps = 0;
    private boolean outOfTargetBlock = false;
    private boolean activedOff = false;
    private boolean isSneaking = false;

    public void onEnable() {
        PlacementUtil.onEnable();
        if (mc.player == null || mc.world == null) {
            disable();
            return;
        }

        if (centerPlayer.getValue() && mc.player.onGround) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        centeredBlock = BlockUtil.getCenterOfBlock(mc.player.posX, mc.player.posY, mc.player.posY);

        oldSlot = mc.player.inventory.currentItem;
    }

    public void onDisable() {
        PlacementUtil.onDisable();
        if (mc.player == null | mc.world == null) return;

        if (outOfTargetBlock) setDisabledMessage("No obsidian detected... Surround turned OFF!");

        if (oldSlot != mc.player.inventory.currentItem && oldSlot != -1 && oldSlot != 9) {
            mc.player.inventory.currentItem = oldSlot;
            oldSlot = -1;
        }

        if (isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        RCCrystalAura.stopAC = false;

        if (offhandObby.getValue() && OffHand.isActive()) {
            OffHand.removeObsidian();
            activedOff = false;
        }

        centeredBlock = Vec3d.ZERO;
        outOfTargetBlock = false;
    }

    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            disable();
            return;
        }

        if (sneakOnly.getValue() && !mc.player.isSneaking()) {
            return;
        }

        if (!(mc.player.onGround) && !(mc.player.isInWeb)) {
            switch (jumpMode.getValue()) {
                case "Pause" : {
                    return;
                }
                case "Disable" : {
                    disable();
                    return;
                }
                default: {
                    break;
                }
            }
        }

        int targetBlockSlot = InventoryUtil.findObsidianSlot(offhandObby.getValue(), activedOff);

        if ((outOfTargetBlock || targetBlockSlot == -1) && disableNoBlock.getValue()) {
            outOfTargetBlock = true;
            disable();
            return;
        }

        activedOff = true;

        if (centerPlayer.getValue() && centeredBlock != Vec3d.ZERO && mc.player.onGround) {
            PlayerUtil.centerPlayer(centeredBlock);
        }

        while (delayTimer.getTimePassed() / 50L >= delayTicks.getValue()) {
            delayTimer.reset();

            int blocksPlaced = 0;

            while (blocksPlaced <= blocksPerTick.getValue()) {
                int maxSteps;
                Vec3d[] offsetPattern;

                switch (offsetMode.getValue()) {
                    case "Anti City" : {
                        offsetPattern = Offsets.SURROUND_CITY;
                        maxSteps = Offsets.SURROUND_CITY.length;
                        break;
                    }
                    default: {
                        offsetPattern = Offsets.SURROUND;
                        maxSteps = Offsets.SURROUND.length;
                        break;
                    }
                }

                if (offsetSteps >= maxSteps) {
                    offsetSteps = 0;
                    break;
                }

                BlockPos offsetPos = new BlockPos(offsetPattern[offsetSteps]);
                BlockPos targetPos = new BlockPos(mc.player.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());

                boolean tryPlacing = true;

                if (mc.player.posY % 1 > 0.2) {
                    targetPos = new BlockPos(targetPos.getX(), targetPos.getY() + 1, targetPos.getZ());
                }

                if (!mc.world.getBlockState(targetPos).getMaterial().isReplaceable()) {
                    tryPlacing = false;
                }

                for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(targetPos))) {
                    if (entity instanceof EntityPlayer) {
                        tryPlacing = false;
                        break;
                    }
                }

                if (tryPlacing && placeBlock(targetPos)) {
                    blocksPlaced++;
                }

                offsetSteps++;

                if (isSneaking) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    isSneaking = false;
                }
            }
        }
    }

    private boolean placeBlock(BlockPos pos) {
        EnumHand handSwing = EnumHand.MAIN_HAND;

        int targetBlockSlot = InventoryUtil.findObsidianSlot(offhandObby.getValue(), activedOff);

        if (targetBlockSlot == -1) {
            outOfTargetBlock = true;
            return false;
        }

        if (targetBlockSlot == 9) {
            activedOff = true;
            if (mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock && ((ItemBlock) mc.player.getHeldItemOffhand().getItem()).getBlock() instanceof BlockObsidian) {
                handSwing = EnumHand.OFF_HAND;
            } else return false;
        }

        if (mc.player.inventory.currentItem != targetBlockSlot && targetBlockSlot != 9) {
            mc.player.inventory.currentItem = targetBlockSlot;
        }

        return PlacementUtil.place(pos, handSwing, rotate.getValue(), true);
    }
}