package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.theme.Theme;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.Comparator;


@Module.Declaration(name = "TargetInfo", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = 0, posZ = 150)
public class TargetInfo extends HUDModule {

    IntegerSetting range = registerInteger("Range", 100, 10, 260);
    ColorSetting backgroundColor = registerColor("Background", new RCColor(0, 0, 0, 255));
    ColorSetting outlineColor = registerColor("Outline", new RCColor(255, 0, 0, 255));

    public void populate(Theme theme) {
        component = new TargetInfoComponent(theme);
    }

    private Color getNameColor(EntityPlayer entityPlayer) {
        if (SocialManager.isFriend(entityPlayer.getName())) {
            return new RCColor(ModuleManager.getModule(ColorMain.class).getFriendGSColor(), 255);
        } else if (SocialManager.isEnemy(entityPlayer.getName())) {
            return new RCColor(ModuleManager.getModule(ColorMain.class).getEnemyGSColor(), 255);
        } else {
            return new RCColor(255, 255, 255, 255);
        }
    }

    private Color getHealthColor(EntityPlayer entityPlayer) {
        int health = (int) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());

        if (health > 36) {
            health = 36;
        }
        if (health < 0) {
            health = 0;
        }

        int red = (int) (255 - (health * 7.0833));
        int green = 255 - red;

        return new Color(red, green, 0, 100);
    }

    private static Color getDistanceColor(EntityPlayer entityPlayer) {
        int distance = (int) entityPlayer.getDistance(mc.player);

        if (distance > 50) {
            distance = 50;
        }

        int red = (int) (255 - (distance * 5.1));
        int green = 255 - red;

        return new Color(red, green, 0, 100);
    }

    public static EntityPlayer targetPlayer;

    public static boolean isRenderingEntity(EntityPlayer entityPlayer) {
        return targetPlayer == entityPlayer;
    }

    private class TargetInfoComponent extends HUDComponent {

        public TargetInfoComponent(Theme theme) {
            super(getName(), theme.getPanelRenderer(), TargetInfo.this.position);
        }

        @Override
        public void render(Context context) {
            super.render(context);

            if (mc.player != null && mc.player.ticksExisted >= 10) {

                EntityPlayer entityPlayer = (EntityPlayer) mc.world.loadedEntityList.stream()
                        .filter(entity -> entity instanceof EntityPlayer)
                        .filter(entity -> entity != mc.player)
                        .map(entity -> (EntityLivingBase) entity)
                        .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                        .orElse(null);

                if (entityPlayer != null && entityPlayer.getDistance(mc.player) <= range.getValue()) {

                    //background
                    Color background = new RCColor(backgroundColor.getValue(), 100);
                    context.getInterface().fillRect(context.getRect(), background, background, background, background);

                    //outline, credit to lukflug for this
                    Color outline = new RCColor(outlineColor.getValue(), 255);
                    context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), outline, outline, outline, outline);
                    context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), outline, outline, outline, outline);
                    context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), outline, outline, outline, outline);
                    context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), outline, outline, outline, outline);

                    //name
                    String name = entityPlayer.getName();
                    Color nameColor = getNameColor(entityPlayer);
                    context.getInterface().drawString(new Point(context.getPos().x + 2, context.getPos().y + 2), name, nameColor);

                    //health box
                    int healthVal = (int) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
                    Color healthBox = getHealthColor(entityPlayer);
                    context.getInterface().fillRect(new Rectangle(context.getPos().x + 32, context.getPos().y + 12, (int) (healthVal * 1.9444), 15), healthBox, healthBox, healthBox, healthBox);

                    //distance box
                    int distanceVal = (int) (entityPlayer.getDistance(mc.player));
                    int width = (int) (distanceVal * 1.38);
                    if (width > 69) {
                        width = 69;
                    }
                    Color distanceBox = getDistanceColor(entityPlayer);
                    context.getInterface().fillRect(new Rectangle(context.getPos().x + 32, context.getPos().y + 27, width, 15), distanceBox, distanceBox, distanceBox, distanceBox);

                    //player model
                    targetPlayer = entityPlayer;
                    RaptorClientGui.renderEntity(entityPlayer, new Point(context.getPos().x + 17, context.getPos().y + 40), 15);

                    //health string
                    String health = "Health: " + healthVal;
                    Color healthColor = new Color(255, 255, 255, 255);
                    context.getInterface().drawString(new Point(context.getPos().x + 33, context.getPos().y + 14), health, healthColor);

                    //distance string
                    String distance = "Distance: " + distanceVal;
                    Color distanceColor = new Color(255, 255, 255, 255);
                    context.getInterface().drawString(new Point(context.getPos().x + 33, context.getPos().y + 29), distance, distanceColor);
                }
            }
        }

        @Override
        public int getWidth(Interface inter) {
            return 102;
        }

        @Override
        public void getHeight(Context context) {
            context.setHeight(43);
        }
    }
}