package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.RenderEntityEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.render.ChamsUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

/**
 * @author Techale
 * @author Hoosiers
 */

@Module.Declaration(name = "Chams", category = Category.Render)
public class Chams extends Module {

    ModeSetting chamsType = registerMode("Type", Arrays.asList("Texture", "Color", "WireFrame"), "Texture");
    IntegerSetting range = registerInteger("Range", 100, 10, 260);
    BooleanSetting player = registerBoolean("Player", true);
    BooleanSetting mob = registerBoolean("Mob", false);
    BooleanSetting crystal = registerBoolean("Crystal", false);
    IntegerSetting lineWidth = registerInteger("Line Width", 1, 1, 5);
    IntegerSetting colorOpacity = registerInteger("Color Opacity", 100, 0, 255);
    IntegerSetting wireOpacity = registerInteger("Wire Opacity", 200, 0, 255);
    ColorSetting playerColor = registerColor("Player Color", new RCColor(0, 255, 255, 255));
    ColorSetting mobColor = registerColor("Mob Color", new RCColor(255, 255, 0, 255));
    ColorSetting crystalColor = registerColor("Crystal Color", new RCColor(0, 255, 0, 255));

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<RenderEntityEvent.Head> renderEntityHeadEventListener = new Listener<>(event -> {
        if (event.getType() == RenderEntityEvent.Type.COLOR && chamsType.getValue().equalsIgnoreCase("Texture")) {
            return;
        } else if (event.getType() == RenderEntityEvent.Type.TEXTURE && (chamsType.getValue().equalsIgnoreCase("Color") || chamsType.getValue().equalsIgnoreCase("WireFrame"))) {
            return;
        }

        if (mc.player == null || mc.world == null) {
            return;
        }

        Entity entity1 = event.getEntity();

        if (entity1.getDistance(mc.player) > range.getValue()) {
            return;
        }

        if (player.getValue() && entity1 instanceof EntityPlayer && entity1 != mc.player) {
            renderChamsPre(new RCColor(playerColor.getValue(), 255), true);
        }

        if (mob.getValue() && (entity1 instanceof EntityCreature || entity1 instanceof EntitySlime || entity1 instanceof EntitySquid)) {
            renderChamsPre(new RCColor(mobColor.getValue(), 255), false);
        }

        if (crystal.getValue() && entity1 instanceof EntityEnderCrystal) {
            renderChamsPre(new RCColor(crystalColor.getValue(), 255), false);
        }
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<RenderEntityEvent.Return> renderEntityReturnEventListener = new Listener<>(event -> {
        if (event.getType() == RenderEntityEvent.Type.COLOR && chamsType.getValue().equalsIgnoreCase("Texture")) {
            return;
        } else if (event.getType() == RenderEntityEvent.Type.TEXTURE && (chamsType.getValue().equalsIgnoreCase("Color") || chamsType.getValue().equalsIgnoreCase("WireFrame"))) {
            return;
        }

        if (mc.player == null || mc.world == null) {
            return;
        }

        Entity entity1 = event.getEntity();

        if (entity1.getDistance(mc.player) > range.getValue()) {
            return;
        }

        if (player.getValue() && entity1 instanceof EntityPlayer && entity1 != mc.player) {
            renderChamsPost(true);
        }

        if (mob.getValue() && (entity1 instanceof EntityCreature || entity1 instanceof EntitySlime || entity1 instanceof EntitySquid)) {
            renderChamsPost(false);
        }

        if (crystal.getValue() && entity1 instanceof EntityEnderCrystal) {
            renderChamsPost(false);
        }
    });

    private void renderChamsPre(RCColor color, boolean isPlayer) {
        switch (chamsType.getValue()) {
            case "Texture": {
                ChamsUtil.createChamsPre();
                break;
            }
            case "Color": {
                ChamsUtil.createColorPre(new RCColor(color, colorOpacity.getValue()), isPlayer);
                break;
            }
            case "WireFrame": {
                ChamsUtil.createWirePre(new RCColor(color, wireOpacity.getValue()), lineWidth.getValue(), isPlayer);
                break;
            }
        }
    }

    private void renderChamsPost(boolean isPlayer) {
        switch (chamsType.getValue()) {
            case "Texture": {
                ChamsUtil.createChamsPost();
                break;
            }
            case "Color": {
                ChamsUtil.createColorPost(isPlayer);
                break;
            }
            case "WireFrame": {
                ChamsUtil.createWirePost(isPlayer);
                break;
            }
        }
    }
}