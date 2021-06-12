package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Module.Declaration(name = "ArrayList", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = 0, posZ = 200)
public class ArrayListModule extends HUDModule {

    BooleanSetting sortUp = registerBoolean("Sort Up", true);
    BooleanSetting sortRight = registerBoolean("Sort Right", false);
    ColorSetting color = registerColor("Color", new RCColor(255, 0, 0, 255));

    private ModuleList list = new ModuleList();

    @Override
    public void populate(Theme theme) {
        component = new ListComponent(getName(), theme.getPanelRenderer(), position, list);
    }

    public void onRender() {
        list.activeModules.clear();
        for (Module module : ModuleManager.getModules()) {
            if (module.isEnabled() && module.isDrawn()) list.activeModules.add(module);
        }
        list.activeModules.sort(Comparator.comparing(module -> -RaptorClient.INSTANCE.raptorGUI.guiInterface.getFontWidth(module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo())));
    }

    private class ModuleList implements HUDList {

        public List<Module> activeModules = new ArrayList<Module>();

        @Override
        public int getSize() {
            return activeModules.size();
        }

        @Override
        public String getItem(int index) {
            Module module = activeModules.get(index);
            return (!module.getHudInfo().equals("")) ? module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo() : module.getName();
        }

        @Override
        public Color getItemColor(int index) {
            RCColor c = color.getValue();
            return Color.getHSBColor(c.getHue() + (color.getRainbow() ? .02f * index : 0), c.getSaturation(), c.getBrightness());
        }

        @Override
        public boolean sortUp() {
            return sortUp.isOn();
        }

        @Override
        public boolean sortRight() {
            return sortRight.isOn();
        }
    }
}