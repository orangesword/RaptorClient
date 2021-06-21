package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.misc.Announcer;
import org.lwjgl.input.Keyboard;

@Module.Declaration(name = "HudEditor", category = Category.CLIENT, drawn = false, Description = "Move positions of HUD elements", toggleMsg = false, bind = Keyboard.KEY_RCONTROL)
public class HUDEditor extends Module {

    public void onEnable() {
        RaptorClient.INSTANCE.raptorGUI.enterHUDEditor();
        Announcer announcer = ModuleManager.getModule(Announcer.class);

        if (announcer.clickGui.getValue() && announcer.isEnabled() && mc.player != null) {
            if (announcer.clientSide.getValue()) {
                MessageBus.sendClientPrefixMessage(Announcer.guiMessage);
            } else {
                MessageBus.sendServerMessage(Announcer.guiMessage);
            }
        }
        disable();
    }
}