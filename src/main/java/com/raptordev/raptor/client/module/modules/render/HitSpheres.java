package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.entity.player.EntityPlayer;

@Module.Declaration(name = "HitSpheres", category = Category.Render)
public class HitSpheres extends Module {

    IntegerSetting range = registerInteger("Range", 100, 10, 260);
    DoubleSetting lineWidth = registerDouble("Line Width", 2, 1, 5);
    IntegerSetting slices = registerInteger("Slices", 20, 10, 30);
    IntegerSetting stacks = registerInteger("Stacks", 15, 10, 20);

    public void onWorldRender(RenderEvent event) {
        mc.world.playerEntities.stream().filter(this::isValidPlayer).forEach(entityPlayer -> {
            double posX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double) mc.timer.renderPartialTicks;
            double posY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double) mc.timer.renderPartialTicks;
            double posZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double) mc.timer.renderPartialTicks;

            RCColor color = findRenderColor(entityPlayer);

            RenderUtil.drawSphere(posX, posY, posZ, 6, slices.getValue(), stacks.getValue(), lineWidth.getValue().floatValue(), color);
        });
    }

    private boolean isValidPlayer(EntityPlayer entityPlayer) {
        if (entityPlayer == mc.player) return false;

        else return entityPlayer.getDistance(mc.player) <= range.getValue();
    }

    private RCColor findRenderColor(EntityPlayer entityPlayer) {
        String name = entityPlayer.getName();
        double distance = mc.player.getDistance(entityPlayer);
        ColorMain colorMain = ModuleManager.getModule(ColorMain.class);

        if (SocialManager.isFriend(name)) return colorMain.getFriendGSColor();
        else if (distance >= 8) return new RCColor(0, 255, 0, 255);
        else if (distance < 8) return new RCColor(255, (int) (mc.player.getDistance(entityPlayer) * 255 / 150), 0, 255);
        else if (SocialManager.isEnemy(name)) return colorMain.getEnemyGSColor();
        else return new RCColor(1, 1, 1, 255);
    }
}