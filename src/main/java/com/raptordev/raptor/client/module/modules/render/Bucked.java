package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.api.util.world.GeometryMasks;
import com.raptordev.raptor.api.util.world.HoleUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 03/10/2021
 */

@Module.Declaration(name = "Bucked", category = Category.Render)
public class Bucked extends Module {

    IntegerSetting range = registerInteger("Range", 100, 10, 260);
    BooleanSetting self = registerBoolean("Self", false);
    BooleanSetting friend = registerBoolean("Friend", true);
    BooleanSetting enemiesOnly = registerBoolean("Only Enemies", false);
    ModeSetting heightMode = registerMode("Height", Arrays.asList("Single", "Double"), "Single");
    ModeSetting renderMode = registerMode("Render", Arrays.asList("Outline", "Fill", "Both"), "Both");
    IntegerSetting width = registerInteger("Line Width", 2, 1, 5);
    ColorSetting color = registerColor("Color", new RCColor(0, 255, 0, 255));

    public void onWorldRender(RenderEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        mc.world.playerEntities.stream().filter(this::isValidTarget).forEach(entityPlayer -> {

            BlockPos blockPos = new BlockPos(roundValueToCenter(entityPlayer.posX), roundValueToCenter(entityPlayer.posY), roundValueToCenter(entityPlayer.posZ));

            if (!isSurrounded(blockPos)) {
                renderESP(blockPos, findGSColor(entityPlayer));
            }
        });
    }

    private boolean isValidTarget(EntityPlayer entityPlayer) {
        if (entityPlayer == null || entityPlayer.isDead || entityPlayer.getHealth() <= 0) return false;

        if (enemiesOnly.getValue() && !SocialManager.isEnemy(entityPlayer.getName())) return false;

        if (entityPlayer.getDistance(mc.player) > range.getValue()) return false;

        if (!self.getValue() && entityPlayer == mc.player) return false;

        if (!friend.getValue() && SocialManager.isFriend(entityPlayer.getName())) return false;

        return true;
    }

    private boolean isSurrounded(BlockPos blockPos) {
        return HoleUtil.isHole(blockPos, true, false).getType() != HoleUtil.HoleType.NONE;
    }

    private void renderESP(BlockPos blockPos, RCColor color) {
        int upValue = heightMode.getValue().equalsIgnoreCase("Double") ? 2 : 1;

        RCColor RCColor1 = new RCColor(color, 255);
        RCColor RCColor2 = new RCColor(color, 50);

        switch (renderMode.getValue()) {
            case "Both": {
                RenderUtil.drawBox(blockPos, upValue, RCColor2, GeometryMasks.Quad.ALL);
                RenderUtil.drawBoundingBox(blockPos, upValue, width.getValue(), RCColor1);
                break;
            }
            case "Outline": {
                RenderUtil.drawBoundingBox(blockPos, upValue, width.getValue(), RCColor1);
                break;
            }
            case "Fill": {
                RenderUtil.drawBox(blockPos, upValue, RCColor2, GeometryMasks.Quad.ALL);
                break;
            }
        }
    }

    private RCColor findGSColor(EntityPlayer entityPlayer) {

        if (SocialManager.isFriend(entityPlayer.getName())) return ModuleManager.getModule(ColorMain.class).getFriendRCColor();
        else if (SocialManager.isEnemy(entityPlayer.getName())) return ModuleManager.getModule(ColorMain.class).getEnemyRCColor();

        return color.getValue();
    }

    private double roundValueToCenter(double inputVal) {
        double roundVal = Math.round(inputVal);

        if (roundVal > inputVal) {
            roundVal -= 0.5;
        } else if (roundVal <= inputVal) {
            roundVal += 0.5;
        }

        return roundVal;
    }
}