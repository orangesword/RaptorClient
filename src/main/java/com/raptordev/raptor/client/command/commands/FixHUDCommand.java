package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

@Command.Declaration(name = "FixHUD", syntax = "fixhud", alias = {"fixhud", "hud", "resethud"})
public class FixHUDCommand extends Command {

    @Override
    public void onCommand(String command, String[] message) {
        for (Module module : ModuleManager.getModules()) {
            if (module instanceof HUDModule) {
                ((HUDModule) module).resetPosition();
            }
        }
        MessageBus.sendCommandMessage("HUD positions reset!", true);
    }
}