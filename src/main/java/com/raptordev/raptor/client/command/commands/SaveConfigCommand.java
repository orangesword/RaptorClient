package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.config.SaveConfig;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;

/**
 * @author Hoosiers
 * @since 1/1/2020
 */

@Command.Declaration(name = "SaveConfig", syntax = "saveconfig", alias = {"saveconfig", "reloadconfig", "config", "saveconfiguration"})
public class SaveConfigCommand extends Command {

    public void onCommand(String command, String[] message) {
        SaveConfig.init();
        MessageBus.sendCommandMessage("Config saved!", true);
    }
}