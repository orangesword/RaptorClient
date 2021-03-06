package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.command.CommandManager;

/**
 * @Author Hoosiers on 11/05/2020
 */

@Command.Declaration(name = "Commands", syntax = "commands", alias = {"commands", "cmd", "command", "commandlist", "help"})
public class CmdListCommand extends Command {

    public void onCommand(String command, String[] message) {
        for (Command command1 : CommandManager.getCommands()) {
            MessageBus.sendCommandMessage(command1.getName() + ": " + "\"" + command1.getSyntax() + "\"!", true);
        }
    }
}