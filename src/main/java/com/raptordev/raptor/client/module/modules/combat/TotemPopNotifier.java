package com.raptordev.raptor.client.module.modules.combat;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.misc.PvPInfo;

@Module.Declaration(name = "TotemPopNotifier", category = Category.Combat, Description = "notifies you when someones pops a toem")
public class TotemPopNotifier extends Module {

    PvPInfo module = ModuleManager.getModule(PvPInfo.class);

    public void onUpdate() {
        module.popCounter.setValue(true);
    }

    protected void onEnable() {
        module.setToggleMsg(false);
        module.enable();
    }

    protected void onDisable() {
        module.disable();
    }
}
