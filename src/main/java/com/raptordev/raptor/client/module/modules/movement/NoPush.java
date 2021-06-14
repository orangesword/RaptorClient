package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.EntityCollisionEvent;
import com.raptordev.raptor.api.event.events.WaterPushEvent;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

@Module.Declaration(name = "NoPush", category = Category.Movement)
public class NoPush extends Module {

    @EventHandler
    private final Listener<EntityCollisionEvent> entityCollisionEventListener = new Listener<>(event -> {
        if (isEnabled()) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<WaterPushEvent> waterPushEventListener = new Listener<>(event -> {
        if (isEnabled()) {
            event.cancel();
        }
    });

}
