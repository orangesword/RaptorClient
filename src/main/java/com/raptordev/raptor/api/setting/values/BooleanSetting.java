package com.raptordev.raptor.api.setting.values;

import com.raptordev.raptor.api.setting.Setting;
import com.raptordev.raptor.client.module.Module;
import com.lukflug.panelstudio.settings.Toggleable;

public class BooleanSetting extends Setting<Boolean> implements Toggleable {

    public BooleanSetting(String name, Module module, boolean value) {
        super(value, name, module);
    }

    @Override
    public void toggle() {
        setValue(!getValue());
    }

    @Override
    public boolean isOn() {
        return getValue();
    }
}