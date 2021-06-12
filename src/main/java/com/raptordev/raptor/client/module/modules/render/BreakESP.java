package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.DrawBlockDamageEvent;
import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.api.util.world.GeometryMasks;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 12/13/2020
 */

@Module.Declaration(name = "BreakESP", category = Category.Render)
public class BreakESP extends Module {

    ModeSetting renderType = registerMode("Render", Arrays.asList("Outline", "Fill", "Both"), "Both");
    IntegerSetting lineWidth = registerInteger("Width", 1, 0, 5);
    IntegerSetting range = registerInteger("Range", 100, 1, 200);
    BooleanSetting cancelAnimation = registerBoolean("No Animation", true);
    ColorSetting color = registerColor("Color", new RCColor(0, 255, 0, 255));

    public void onWorldRender(RenderEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null) {

                BlockPos blockPos = destroyBlockProgress.getPosition();

                if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    return;
                }

                if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.getValue()) {

                    int progress = destroyBlockProgress.getPartialBlockDamage();
                    AxisAlignedBB axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

                    renderESP(axisAlignedBB, progress, color.getValue());
                }
            }
        });
    }

    private void renderESP(AxisAlignedBB axisAlignedBB, int progress, RCColor color) {
        RCColor fillColor = new RCColor(color, 50);
        RCColor outlineColor = new RCColor(color, 255);

        double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
        double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
        double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
        double progressValX = progress * ((axisAlignedBB.maxX - centerX) / 10);
        double progressValY = progress * ((axisAlignedBB.maxY - centerY) / 10);
        double progressValZ = progress * ((axisAlignedBB.maxZ - centerZ) / 10);

        AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);

        switch (renderType.getValue()) {
            case "Fill": {
                RenderUtil.drawBox(axisAlignedBB1, true, 0, fillColor, GeometryMasks.Quad.ALL);
                break;
            }
            case "Outline": {
                RenderUtil.drawBoundingBox(axisAlignedBB1, lineWidth.getValue(), outlineColor);
                break;
            }
            case "Both": {
                RenderUtil.drawBox(axisAlignedBB1, true, 0, fillColor, GeometryMasks.Quad.ALL);
                RenderUtil.drawBoundingBox(axisAlignedBB1, lineWidth.getValue(), outlineColor);
                break;
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<DrawBlockDamageEvent> drawBlockDamageEventListener = new Listener<>(event -> {
       if (cancelAnimation.getValue()) {
           event.cancel();
       }
    });
}