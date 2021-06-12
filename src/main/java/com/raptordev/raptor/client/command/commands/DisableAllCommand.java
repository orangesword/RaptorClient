package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

/**
 * @Author Hoosiers on 11/05/2020
 */

@Command.Declaration(name = "DisableAll", syntax = "disableall", alias = {"disableall", "stop"})
public class DisableAllCommand extends Command {

    public void onCommand(String command, String[] message) {
        int count = 0;

        for (Module module : ModuleManager.getModules()) {
            if (module.isEnabled()) {
                module.disable();
                count++;
            }
        }

        MessageBus.sendCommandMessage("Disabled " + count + " modules!", true);
    }
}