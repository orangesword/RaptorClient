package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;

import java.awt.*;

@Module.Declaration(name = "Watermark", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = 0, posZ = 0)
public class Watermark extends HUDModule {

    private ColorSetting color = registerColor("Color", new RCColor(255, 0, 0, 255));

    @Override
    public void populate(Theme theme) {
        component = new ListComponent(getName(), theme.getPanelRenderer(), position, new WatermarkList());
    }

    private class WatermarkList implements HUDList {

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String getItem(int index) {
            return "RaptorClient " + RaptorClient.MODVER;
        }

        @Override
        public Color getItemColor(int index) {
            return color.getValue();
        }

        @Override
        public boolean sortUp() {
            return false;
        }

        @Override
        public boolean sortRight() {
            return false;
        }
    }
}