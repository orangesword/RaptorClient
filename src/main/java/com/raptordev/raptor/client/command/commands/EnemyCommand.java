package com.raptordev.raptor.client.command.commands;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.client.command.Command;

@Command.Declaration(name = "Enemy", syntax = "enemy list/add/del [player]", alias = {"enemy", "enemies", "e"})
public class EnemyCommand extends Command {

    public void onCommand(String command, String[] message) {
        String main = message[0];

        if (main.equalsIgnoreCase("list")) {
            MessageBus.sendClientPrefixMessage("Enemies: " + SocialManager.getEnemiesByName() + "!");
            return;
        }

        String value = message[1];

        if (main.equalsIgnoreCase("add") && !SocialManager.isEnemy(value)) {
            SocialManager.addEnemy(value);
            MessageBus.sendCommandMessage("Added enemy: " + value.toUpperCase() + "!", true);
        } else if (main.equalsIgnoreCase("del") && SocialManager.isEnemy(value)) {
            SocialManager.delEnemy(value);
            MessageBus.sendCommandMessage("Deleted enemy: " + value.toUpperCase() + "!", true);
        }
    }
}