package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketCloseWindow;

/**
 * @author Soulbond
 * @since 02/28/2021
 */

@Module.Declaration(name = "XCarry", category = Category.Misc)
public class XCarry extends Module {

    @SuppressWarnings("unused")
    @EventHandler
    private Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            if (((CPacketCloseWindow) event.getPacket()).windowId == mc.player.inventoryContainer.windowId) {
                event.cancel();
            }
        }
    });
}