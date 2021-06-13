package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.modules.misc.AutoSpam;

@Command.Declaration(name = "AutoSpam", syntax = "autospam add/del [message] (use _ for spaces)", alias = {"autospam","spam"})
public class AutoSpamCommand extends Command {
    @Override
    public void onCommand(String command, String[] message) {
        String main = message[0];
        String value = message[1].replace("_", " ");

        if (main.equalsIgnoreCase("add") && !(AutoSpam.spamMessages.contains(value))) {
            AutoSpam.spamMessages.add(value);
            MessageBus.sendCommandMessage("Added AutoSpam message: " + value + "!", true);
        } else if (main.equalsIgnoreCase("del") && AutoSpam.spamMessages.contains(value)) {
            AutoSpam.spamMessages.remove(value);
            MessageBus.sendCommandMessage("Deleted AutoSpam message: " + value + "!", true);
        }

    }
}
