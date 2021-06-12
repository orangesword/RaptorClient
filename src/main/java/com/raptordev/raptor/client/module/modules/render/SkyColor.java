package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.EntityViewRenderEvent;

@Module.Declaration(name = "SkyColor", category = Category.Render)
public class SkyColor extends Module {

    BooleanSetting fog = registerBoolean("Fog", true);
    ColorSetting color = registerColor("Color", new RCColor(0, 255, 0, 255));

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<EntityViewRenderEvent.FogColors> fogColorsListener = new Listener<>(event -> {
       event.setRed(color.getValue().getRed() / 255.0F);
       event.setGreen(color.getValue().getGreen() / 255.0F);
       event.setBlue(color.getValue().getBlue() / 255.0F);
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<EntityViewRenderEvent.FogDensity> fogDensityListener = new Listener<>(event -> {
       if (!fog.getValue()) {
           event.setDensity(0);
           event.setCanceled(true);
       }
    });
}