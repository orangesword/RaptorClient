package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.api.config.SaveConfig;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.combat.RCCrystalAura;


@Module.Declaration(name = "BetterConfig", category = Category.CLIENT, Description = "Save your configs in better way", alwaysEnabled = true)
public class BetterConfig extends Module {

    IntegerSetting delay = registerInteger("Timer", 300,60,600);
    BooleanSetting send = registerBoolean("SendMessage", true);
    public BooleanSetting disable = registerBoolean("CpvpDisable", false);

    Timer timer = new Timer();

    public void onUpdate() {
        if (timer.getTimePassed() / 1000L >= delay.getValue().longValue()) {
            SaveConfig.init();
            if (send.getValue()) {
                MessageBus.sendClientPrefixMessage("ConfigSaved");
            }

            if (disable.getValue() && ModuleManager.getModule(RCCrystalAura.class).isAttacking) {
                SaveConfig.init();
                disable();
                MessageBus.sendClientPrefixMessage("Saved Config last time Because of pvp");
            }

            timer.reset();
        }
    }

    protected void onEnable() {
        timer.reset();
    }

    protected void onDisable() {
        timer.reset();
    }
}
