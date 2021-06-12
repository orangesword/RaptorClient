package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ClickGuiModule;
import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.SettingsAnimation;
import com.lukflug.panelstudio.tabgui.*;
import com.lukflug.panelstudio.theme.SettingsColorScheme;
import com.lukflug.panelstudio.theme.Theme;
import org.lwjgl.input.Keyboard;

/**
 * @author lukflug
 */

@Module.Declaration(name = "TabGUI", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = RaptorClientGui.DISTANCE, posZ = RaptorClientGui.DISTANCE)
public class TabGUIModule extends HUDModule {

    @Override
    public void populate(Theme theme) {
        ClickGuiModule clickGuiModule = ModuleManager.getModule(ClickGuiModule.class);
        TabGUIRenderer renderer = new DefaultRenderer(new SettingsColorScheme(clickGuiModule.enabledColor, clickGuiModule.backgroundColor, clickGuiModule.settingBackgroundColor, clickGuiModule.backgroundColor, clickGuiModule.fontColor, clickGuiModule.opacity), RaptorClientGui.HEIGHT, 5, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_RETURN);
        TabGUI component = new TabGUI("TabGUI", renderer, new Animation() {
            @Override
            protected int getSpeed() {
                return clickGuiModule.animationSpeed.getValue();
            }
        }, position, 75);
        for (Category category : Category.values()) {
            TabGUIContainer tab = new TabGUIContainer(category.name(), renderer, new SettingsAnimation(clickGuiModule.animationSpeed));
            component.addComponent(tab);
            for (Module module : ModuleManager.getModulesInCategory(category)) {
                tab.addComponent(new TabGUIItem(module.getName(), module));
            }
        }
        this.component = component;
    }
}