package com.raptordev.raptor.client.command.commands;

//import baritone.Baritone;
//import baritone.api.BaritoneAPI;
//import baritone.api.IBaritoneProvider;
//import baritone.api.command.datatypes.ForBlockOptionalMeta;
//import baritone.api.pathing.goals.GoalXZ;
//import baritone.api.utils.BlockOptionalMeta;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.command.Command;


@Command.Declaration(name = "baritone", syntax = "baritone goto (use _ for space)", alias = {"baritone"})
public class BaritoneCommand extends Command {
    @Override
    public void onCommand(String command, String[] message) {
        String main = message[0];
        String value = message[1].replace("_", " ");
//        if (main.equalsIgnoreCase("goto")) {
//            String[] xz = value.split(" ");
//            String x = xz[0];
//            String z = xz[1];
//            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ(Integer.parseInt(x), Integer.parseInt(z)));
//            MessageBus.sendCommandMessage("Going to X" + x + "Z" + z, true);
//        }
    }
}
