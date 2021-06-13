package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.misc.Timer;
import java.util.*;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(name = "AutoSpam", category = Category.Misc, Description = "Spam in server")
public class AutoSpam extends Module {

    public static AutoSpam instance = new AutoSpam();
    public static final List<String> spamMessages = new ArrayList<String>();
    public ModeSetting mode = registerMode("Mode", Arrays.asList("Everyone", "Me"), "Everyone");
    public IntegerSetting delay = registerInteger("Delay", 30, 10, 300);
    public IntegerSetting index = registerInteger("Message Number", 1,0,10);


    private final Timer timer = new Timer();

    public void onUpdate() {

        String msg;

        if (timer.getTimePassed() / 1000L >= delay.getValue().longValue()) {
            if (mode.getValue().equals("Everyone")) {

                if (spamMessages.size() >= index.getValue()) {
                    msg = spamMessages.get(index.getValue());
                } else msg = "Download RaptorClient";

                MessageBus.sendServerMessage(msg);
                timer.reset();
            } else if (mode.getValue().equals("Me")){

                if (spamMessages.size() > 1) {
                    msg = spamMessages.get(index.getValue());
                } else msg = "Download RaptorClient";
                MessageBus.sendClientPrefixMessage(msg);
                timer.reset();
            }
        }
    }


    protected void onEnable() {
        timer.reset();
    }

    public void onDisable() {
        this.timer.reset();
    }

}
