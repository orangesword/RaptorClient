package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.api.util.world.GeometryMasks;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.Arrays;

/**
 * @Author Hoosiers on 10/10/2020
 */

@Module.Declaration(name = "BlockHighlight", category = Category.Render)
public class BlockHighlight extends Module {

    ModeSetting renderLook = registerMode("Render", Arrays.asList("Block", "Side"), "Block");
    ModeSetting renderType = registerMode("Type", Arrays.asList("Outline", "Fill", "Both"), "Outline");
    IntegerSetting lineWidth = registerInteger("Width", 1, 1, 5);
    ColorSetting renderColor = registerColor("Color", new RCColor(255, 0, 0, 255));

    private int lookInt;

    public void onWorldRender(RenderEvent event) {
        RayTraceResult rayTraceResult = mc.objectMouseOver;

        if (rayTraceResult == null) {
            return;
        }

        EnumFacing enumFacing = mc.objectMouseOver.sideHit;

        if (enumFacing == null) {
            return;
        }

        AxisAlignedBB axisAlignedBB;
        BlockPos blockPos;

        RCColor colorWithOpacity = new RCColor(renderColor.getValue(), 50);

        switch (renderLook.getValue()) {
            case "Block": {
                lookInt = 0;
                break;
            }

            case "Side": {
                lookInt = 1;
                break;
            }
        }

        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            blockPos = rayTraceResult.getBlockPos();
            axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

            if (axisAlignedBB != null && blockPos != null && mc.world.getBlockState(blockPos).getMaterial() != Material.AIR) {
                switch (renderType.getValue()) {
                    case "Outline": {
                        renderOutline(axisAlignedBB, lineWidth.getValue(), renderColor.getValue(), enumFacing, lookInt);
                        break;
                    }
                    case "Fill": {
                        renderFill(axisAlignedBB, colorWithOpacity, enumFacing, lookInt);
                        break;
                    }

                    case "Both": {
                        renderOutline(axisAlignedBB, lineWidth.getValue(), renderColor.getValue(), enumFacing, lookInt);
                        renderFill(axisAlignedBB, colorWithOpacity, enumFacing, lookInt);
                        break;
                    }
                }
            }
        }
    }

    public void renderOutline(AxisAlignedBB axisAlignedBB, int width, RCColor color, EnumFacing enumFacing, int lookInt) {

        if (lookInt == 0) {
            RenderUtil.drawBoundingBox(axisAlignedBB, width, color);
        } else if (lookInt == 1) {
            RenderUtil.drawBoundingBoxWithSides(axisAlignedBB, width, color, findRenderingSide(enumFacing));
        }
    }

    public void renderFill(AxisAlignedBB axisAlignedBB, RCColor color, EnumFacing enumFacing, int lookInt) {
        int facing = 0;

        if (lookInt == 0) {
            facing = GeometryMasks.Quad.ALL;
        } else if (lookInt == 1) {
            facing = findRenderingSide(enumFacing);
        }

        RenderUtil.drawBox(axisAlignedBB, true, 1, color, facing);
    }

    private int findRenderingSide(EnumFacing enumFacing) {
        int facing = 0;

        if (enumFacing == EnumFacing.EAST) {
            facing = GeometryMasks.Quad.EAST;
        } else if (enumFacing == EnumFacing.WEST) {
            facing = GeometryMasks.Quad.WEST;
        } else if (enumFacing == EnumFacing.NORTH) {
            facing = GeometryMasks.Quad.NORTH;
        } else if (enumFacing == EnumFacing.SOUTH) {
            facing = GeometryMasks.Quad.SOUTH;
        } else if (enumFacing == EnumFacing.UP) {
            facing = GeometryMasks.Quad.UP;
        } else if (enumFacing == EnumFacing.DOWN) {
            facing = GeometryMasks.Quad.DOWN;
        }

        return facing;
    }
}