package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.ColorUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

@Module.Declaration(name = "Colors", category = Category.CLIENT, drawn = false, Description = "Change GUI colours and fonts", toggleMsg = false)
public class ColorMain extends Module {

    public BooleanSetting customFont = registerBoolean("Custom Font", false);
    public BooleanSetting textFont = registerBoolean("Custom Text", false);
    public ModeSetting friendColor = registerMode("Friend Color", ColorUtil.colors, "Blue");
    public ModeSetting enemyColor = registerMode("Enemy Color", ColorUtil.colors, "Red");
    public ModeSetting chatEnableColor = registerMode("Msg Enbl", ColorUtil.colors, "Green");
    public ModeSetting chatDisableColor = registerMode("Msg Dsbl", ColorUtil.colors, "Red");
    public ModeSetting colorModel = registerMode("Color Type", Arrays.asList("RGB", "HSB"), "RGB");

    public void onEnable() {
        this.disable();
    }

    public TextFormatting getFriendColor() {
        return ColorUtil.settingToTextFormatting(friendColor);
    }

    public TextFormatting getEnemyColor() {
        return ColorUtil.settingToTextFormatting(enemyColor);
    }

    public TextFormatting getEnabledColor() {
        return ColorUtil.settingToTextFormatting(chatEnableColor);
    }

    public TextFormatting getDisabledColor() {
        return ColorUtil.settingToTextFormatting(chatDisableColor);
    }

    public RCColor getFriendRCColor() {
        return new RCColor(ColorUtil.settingToColor(friendColor));
    }

    public RCColor getEnemyRCColor() {
        return new RCColor(ColorUtil.settingToColor(enemyColor));
    }
}