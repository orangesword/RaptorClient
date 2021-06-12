package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.config.SaveConfig;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @Author Hoosiers on 11/05/2020
 */

@Command.Declaration(name = "OpenFolder", syntax = "openfolder", alias = {"openfolder", "config", "open", "folder"})
public class OpenFolderCommand extends Command {

    public void onCommand(String command, String[] message) {
        try {
            Desktop.getDesktop().open(new File(SaveConfig.fileName.replace("/", "")));
            MessageBus.sendCommandMessage("Opened config folder!", true);
        } catch (IOException e) {
            MessageBus.sendCommandMessage("Could not open config folder!", true);
            e.printStackTrace();
        }
    }
}