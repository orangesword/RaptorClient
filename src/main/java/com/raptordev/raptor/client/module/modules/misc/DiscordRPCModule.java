package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.manager.managers.DiscordManager;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(name = "DiscordRPC", category = Category.CLIENT, drawn = false)
public class DiscordRPCModule extends Module {

    public void onEnable() {

        DiscordManager.stopRPC();
    }

    public void onDisable() {
        DiscordManager.startRPC();
    }
}