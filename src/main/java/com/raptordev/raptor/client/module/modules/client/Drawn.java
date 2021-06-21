package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

@Module.Declaration(category = Category.CLIENT, name = "Drawn", Description = "Set all modules to drawn")
public class Drawn extends Module {

    protected void onEnable() {
        ModuleManager.getModules().forEach(Module -> {
            Module.setDrawn(true);
        });
    }

    protected void onDisable() {
        ModuleManager.getModules().forEach(Module -> {
            Module.setDrawn(false);
        });
    }

}
