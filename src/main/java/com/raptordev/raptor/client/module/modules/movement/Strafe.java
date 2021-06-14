package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

@Module.Declaration(name = "Strafe", category = Category.Movement)
public class Strafe extends Module {

    Speed module = ModuleManager.getModule(Speed.class);

    public void onUpdate() {
        module.mode.setValue("Strafe");
    }

    protected void onEnable() {
        module.enable();
        module.setToggleMsg(false);
    }

    protected void onDisable() {
        if (module.isEnabled()) {
            module.disable();
        }
        module.setToggleMsg(module.isToggleMsg());
    }
}
