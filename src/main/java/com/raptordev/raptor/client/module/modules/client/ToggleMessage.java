package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

@Module.Declaration(name = "ToggleMessages", category = Category.CLIENT, Description = "Set ToggleMessages of Every Module", enabled = true)
public class ToggleMessage extends Module {

    protected void onEnable() {
        ModuleManager.getModules().forEach(Module -> {
            Module.setToggleMsg(true);
        });
    }

    protected void onDisable() {
        ModuleManager.getModules().forEach(Module -> {
            Module.setToggleMsg(false);
        });
    }

}
