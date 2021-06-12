package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

/**
 * @author Hoosiers
 * @since 03/09/2021
 *
 * Shout out to lyneez because I had to close his PR :C
 */

@Command.Declaration(name = "Msgs", syntax = "msgs [module]", alias = {"msgs", "togglemsgs", "showmsgs", "messages"})
public class MsgsCommand extends Command {


    public void onCommand(String command, String[] message) {
        String main = message[0];

        Module module = ModuleManager.getModule(main);

        if (module == null) {
            MessageBus.sendCommandMessage(this.getSyntax(), true);
            return;
        }

        if (module.isToggleMsg()) {
            module.setToggleMsg(false);
            MessageBus.sendCommandMessage("Module " + module.getName() + " message toggle set to: FALSE!", true);
        } else if (!module.isToggleMsg()) {
            module.setToggleMsg(true);
            MessageBus.sendCommandMessage("Module " + module.getName() + " message toggle set to: TRUE!", true);
        }
    }
}