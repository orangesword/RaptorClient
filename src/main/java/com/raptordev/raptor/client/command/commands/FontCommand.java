package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.font.CFontRenderer;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.command.Command;

import java.awt.*;


@Command.Declaration(name = "Font", syntax = "font [name] size (use _ for spaces)", alias = {"font", "setfont", "customfont", "fonts", "chatfont"})
public class FontCommand extends Command {

    public void onCommand(String command, String[] message) {
        String main = message[0].replace("_", " ");
        int value = Integer.parseInt(message[1]);

        if (value >= 21 || value <= 15) {
            value = 18;
        }

        RaptorClient.INSTANCE.cFontRenderer = new CFontRenderer(new Font(main, Font.PLAIN, value), true, true);
        RaptorClient.INSTANCE.cFontRenderer.setFontName(main);
        RaptorClient.INSTANCE.cFontRenderer.setFontSize(value);

        MessageBus.sendCommandMessage("Font set to: " + main.toUpperCase() + ", size " + value + "!", true);
    }
}