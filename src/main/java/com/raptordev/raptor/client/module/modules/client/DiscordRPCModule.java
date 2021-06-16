package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.api.manager.managers.DiscordManager;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(name = "DiscordRPC", category = Category.CLIENT, drawn = false)
public class DiscordRPCModule extends Module {

    public void onEnable() {

        MessageBus.sendClientPrefixMessage("Discord RPC is already Enabled and its automatic");

    }

    public void onDisable() {
        DiscordManager.startRPC();
    }
}