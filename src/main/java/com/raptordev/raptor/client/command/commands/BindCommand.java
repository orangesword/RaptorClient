package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import org.lwjgl.input.Keyboard;


@Command.Declaration(name = "Bind", syntax = "bind [module] key", alias = {"bind", "setbind", "key"})
public class BindCommand extends Command {

    public void onCommand(String command, String[] message) {
        String main = message[0];
        String value = message[1].toUpperCase();

        for (Module module : ModuleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(main)) {
                if (value.equalsIgnoreCase("none")) {
                    module.setBind(Keyboard.KEY_NONE);
                    MessageBus.sendCommandMessage("Module " + module.getName() + " bind set to: " + value + "!", true);
                }
                //keeps people from accidentally binding things such as ESC, TAB, exc.
                else if (value.length() == 1) {
                    int key = Keyboard.getKeyIndex(value);

                    module.setBind(key);
                    MessageBus.sendCommandMessage("Module " + module.getName() + " bind set to: " + value + "!", true);
                } else if (value.length() > 1) {
                    MessageBus.sendCommandMessage(this.getSyntax(), true);
                }
            }
        }
    }
}