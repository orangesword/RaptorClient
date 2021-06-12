package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.config.ReadFile;
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

    public ModeSetting mode = registerMode("Mode", Arrays.asList("Everyone", "Me"), "Everyone");
    public DoubleSetting delay = registerDouble("Dely", 10, 5, 50);

    private static final String fileName = "RaptorClient/Misc/Spammer.txt";
    private static final String defaultMessage = "gg";
    private static final List<String> spamMessages = new ArrayList<String>();
    private static final Random rnd = new Random();
    private final Timer timer = new Timer();
    private final List<String> sendPlayers = new ArrayList<String>();

    protected void onEnable() {
        readSpamFile();
    }

    public void onUpdate() {


        if (timer.hasReached(delay.getValue().longValue())) {
            if (mode.getValue().equals("Everyone")) {
                MessageBus.sendServerMessage("Download RaptorClient");
            }

            else MessageBus.sendClientRawMessage("Download RaptorClient");

        }

    }

    public void onDisable() {
        spamMessages.clear();
        this.timer.reset();
    }




    private void readSpamFile() {
        List<String> fileInput = ReadFile.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        spamMessages.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            spamMessages.add(s);
        }
        if (spamMessages.size() == 0) {
            spamMessages.add(defaultMessage);
        }
    }

}
