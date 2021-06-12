package com.raptordev.raptor.api.setting.values;

import com.raptordev.raptor.api.setting.Setting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Module;

import java.awt.*;

public class ColorSetting extends Setting<RCColor> implements com.lukflug.panelstudio.settings.ColorSetting {

    private boolean rainbow;

    public ColorSetting(String name, Module module, boolean rainbow, RCColor value) {
        super(value, name, module);

        this.rainbow = rainbow;
    }

    @Override
    public RCColor getValue() {
        if (rainbow) return RCColor.fromHSB((System.currentTimeMillis() % (360 * 32)) / (360f * 32), 1, 1);
        else return super.getValue();
    }

    public int toInteger() {
        return getValue().getRGB() & 0xFFFFFF + (this.rainbow ? 1 : 0) * 0x1000000;
    }

    public void fromInteger(int number) {
        this.rainbow = ((number & 0x1000000) != 0);

        super.setValue(this.rainbow ? RCColor.fromHSB((System.currentTimeMillis() % (360 * 32)) / (360f * 32), 1, 1) : new RCColor(number & 0xFFFFFF));
    }

    @Override
    public void setValue(Color value) {
        super.setValue(new RCColor(value));
    }

    @Override
    public Color getColor() {
        return super.getValue();
    }

    @Override
    public boolean getRainbow() {
        return this.rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}