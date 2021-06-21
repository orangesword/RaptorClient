package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.misc.Announcer;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

@Module.Declaration(name = "ClickGUI", category = Category.CLIENT, bind = Keyboard.KEY_RSHIFT, drawn = false, Description = "Allows you to change settings in a GUI thanks to PanelStudio", toggleMsg = false)
public class ClickGuiModule extends Module {

    public ModeSetting theme = registerMode("Theme", Arrays.asList("RaptorTheme", "ClearGradientTheme", "GamesenseTheme", "ClearTheme", "PostmanTheme", "BigTheme"), "RaptorTheme");
    public ModeSetting scrolling = registerMode("Scrolling", Arrays.asList("Screen", "Container"), "Screen");

    public IntegerSetting opacity = registerInteger("Opacity", 150, 50, 255);
    public IntegerSetting scrollSpeed = registerInteger("Scroll Speed", 10, 1, 20);
    public IntegerSetting animationSpeed = registerInteger("Animation Speed", 200, 0, 1000);

    public ColorSetting outlineColor = registerColor("Outline", new RCColor(255, 0, 0, 255));
    public ColorSetting enabledColor = registerColor("Enabled", new RCColor(255, 0, 0, 255));
    public ColorSetting backgroundColor = registerColor("Background", new RCColor(0, 0, 0, 255));
    public ColorSetting  settingBackgroundColor = registerColor("Setting", new RCColor(30, 30, 30, 255));
    public ColorSetting fontColor = registerColor("Font", new RCColor(255, 255, 255, 255));

    public BooleanSetting showHUD = registerBoolean("Show HUD types", false);
    public BooleanSetting ignoreDisabled = registerBoolean("IgnoreDisabled", true);
    public BooleanSetting buttonRainbow = registerBoolean("ButtonRainbow", false);

    public void onEnable() {
        RaptorClient.INSTANCE.raptorGUI.enterGUI();
        Announcer announcer = ModuleManager.getModule(Announcer.class);

        if (announcer.clickGui.getValue() && announcer.isEnabled() && mc.player != null) {
            if (announcer.clientSide.getValue()) {
                MessageBus.sendClientPrefixMessage(Announcer.guiMessage);
            } else {
                MessageBus.sendServerMessage(Announcer.guiMessage);
            }
        }
        disable();
    }
}