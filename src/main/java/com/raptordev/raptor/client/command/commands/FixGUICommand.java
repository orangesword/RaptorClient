package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.command.Command;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ClickGuiModule;
import org.lwjgl.input.Keyboard;

@Command.Declaration(name = "FixGUI", syntax = "fixgui", alias = {"fixgui", "gui", "resetgui"})
public class FixGUICommand extends Command {

    public void onCommand(String command, String[] message) {
        RaptorClient.INSTANCE.raptorGUI = new RaptorClientGui();
        ModuleManager.getModule(ClickGuiModule.class).setBind(Keyboard.KEY_RSHIFT);
        MessageBus.sendCommandMessage("ClickGUI positions and bind reset!", true);
    }
}