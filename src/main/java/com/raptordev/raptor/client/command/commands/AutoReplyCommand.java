package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.modules.misc.AutoReply;


@Command.Declaration(name = "AutoReply", syntax = "autoreply set [message] (use _ for spaces)", alias = {"autoreply", "reply"})
public class AutoReplyCommand extends Command {

    public void onCommand(String command, String[] message) {
        String main = message[0];
        String value = message[1].replace("_", " ");

        if (main.equalsIgnoreCase("set")) {
            AutoReply.setReply(value);
            MessageBus.sendCommandMessage("Set AutoReply message: " + value + "!", true);
        }
    }
}