package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Declaration(name = "NoFall", category = Category.Movement)
public class NoFall extends Module {

    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (isEnabled() && event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance >= 3.0) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.onGround = true;
        }
    });

}
