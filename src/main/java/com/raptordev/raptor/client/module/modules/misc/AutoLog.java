package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.player.PlayerUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(category = Category.Misc, name = "AutoLog", Description = "Logout automatically if met certain conditions")
public class AutoLog extends Module {

    public IntegerSetting minimum = registerInteger("MinimumHealt", 10,1,36);


    public void onUpdate() {
        if (mc.player.capabilities.isCreativeMode) return;

        if (mc.isSingleplayer()) return;

        if (PlayerUtil.getHealth() <= minimum.getValue()) {
            mc.world.sendQuittingDisconnectingPacket();
            disable();
        }
    }

}
