package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

@Module.Declaration(name = "GuiMove", category = Category.Movement)
public class GuiMove extends Module {

    PlayerTweaks module = ModuleManager.getModule(PlayerTweaks.class);

    public void onUpdate() {
        module.guiMove.setValue(true);
    }

    protected void onEnable() {
        module.setToggleMsg(false);
        module.enable();
    }

    protected void onDisable() {
        if (module.isEnabled()) {
            module.disable();
        }
    }
}
