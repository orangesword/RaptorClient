package com.raptordev.raptor.client.module.modules.combat;

import com.raptordev.raptor.api.manager.managers.TotemPopManager;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.ColorUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
@Module.Declaration(name = "TotemPopNotifier", category = Category.Combat, Description = "notifies you when someones pops a toem")
public class TotemPopNotifier extends Module {

	ModeSetting chatColor = registerMode("Color", ColorUtil.colors, "Light Purple");

    public void onUpdate() {
    	if (mc.player == null || mc.world == null) {
            return;
        }

        TotemPopManager.INSTANCE.sendMsgs = true;
         TotemPopManager.INSTANCE.chatFormatting = ColorUtil.textToChatFormatting(chatColor);

    }

    protected void onEnable() {
        TotemPopManager.INSTANCE.sendMsgs = true;
    }

    protected void onDisable() {
        TotemPopManager.INSTANCE.sendMsgs = false;
    }
}
