package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;


@Module.Declaration(name = "AntiHunger", category = Category.Movement)
public class AntiHunger extends Module {
	
	public void onUpdate() {
    	if(mc.player.isPotionActive(Potion.getPotionById(17))) {
    		mc.player.removePotionEffect(Potion.getPotionById(17));
    	}
	}
	
    @EventHandler
    public Listener<PacketEvent.Send> packetListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer) event.getPacket()).onGround = false;
        }
    });

}
