package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.InputUpdateEvent;

@Module.Declaration(name = "NoSlow", category = Category.Movement)
public class NoSlow extends Module {

    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
        if (isEnabled()) {
            if (mc.player.isHandActive() && !mc.player.isRiding()) {
                event.getMovementInput().moveStrafe *= 5;
                event.getMovementInput().moveForward *= 5;
            }
        }
    });

}
