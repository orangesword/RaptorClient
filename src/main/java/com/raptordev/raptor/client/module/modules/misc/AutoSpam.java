package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.misc.Timer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.StringUtils;

import java.security.MessageDigest;
import java.util.*;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(name = "AutoSpam", category = Category.Misc, Description = "Spam in server")
public class AutoSpam extends Module {

    public static AutoSpam instance = new AutoSpam();

    public ModeSetting mode = registerMode("Mode", Arrays.asList("Everyone", "Me"), "Everyone");
    public DoubleSetting delay = registerDouble("Delay", 30, 10, 300);

    private static final String fileName = "RaptorClient/Misc/Spammer.txt";
    private static final List<String> spamMessages = new ArrayList<String>();
    private static final Random rnd = new Random();
    private final Timer timer = new Timer();
    private final List<String> sendPlayers = new ArrayList<String>();

    public void onUpdate() {

        String msg;

        if (timer.getTimePassed() / 1000L >= delay.getValue()) {
            if (mode.getValue().equals("Everyone")) {

                if (spamMessages.size() > 1) {
                    msg = spamMessages.get(rnd.nextInt());
                } else msg = "Download RaptorClient";

                MessageBus.sendServerMessage(msg);
            } else {

                if (spamMessages.size() > 1) {
                    msg = spamMessages.get(rnd.nextInt());
                } else msg = "Download RaptorClient";
            }

            MessageBus.sendClientPrefixMessage(msg);
        }
    }



    public void onDisable() {
        this.timer.reset();
    }

}
