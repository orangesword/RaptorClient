package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

@Module.Declaration(category = Category.Misc, name = "AutoReconnnet", Description = "After disconnecting join server automatically")
public class AutoReconnet extends Module{

	public IntegerSetting delay  = registerInteger("delay", 1, 1, 10);
	public Timer timer = new Timer();
    public static ServerData lastestServerData;
	
	public void onUpdate() {
        if(mc.world != null && mc.getCurrentServerData() != null){
            lastestServerData = mc.getCurrentServerData();
        }
		if (mc.currentScreen instanceof GuiDisconnected) {
			if (timer.hasPassedS(delay.getValue().longValue())) {
				GuiConnecting GuiConnecting = new GuiConnecting(new GuiMainMenu(), mc, lastestServerData);
				mc.displayGuiScreen(GuiConnecting);
			}
		}

	}
	
}
