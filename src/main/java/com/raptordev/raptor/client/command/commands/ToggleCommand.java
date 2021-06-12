package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;


@Command.Declaration(name = "Toggle", syntax = "toggle [module]", alias = {"toggle", "t", "enable", "disable"})
public class ToggleCommand extends Command {

    public void onCommand(String command, String[] message) {
        String main = message[0];

        Module module = ModuleManager.getModule(main);

        if (module == null) {
            MessageBus.sendCommandMessage(this.getSyntax(), true);
            return;
        }

        module.toggle();

        if (module.isEnabled()) {
            MessageBus.sendCommandMessage("Module " + module.getName() + " set to: ENABLED!", true);
        } else {
            MessageBus.sendCommandMessage("Module " + module.getName() + " set to: DISABLED!", true);
        }
    }
}