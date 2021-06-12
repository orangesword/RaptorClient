package com.raptordev.raptor.api.manager;

import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.api.manager.managers.ClientEventManager;
import com.raptordev.raptor.api.manager.managers.PlayerPacketManager;
import com.raptordev.raptor.api.manager.managers.TotemPopManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class ManagerLoader {

    private static final List<Manager> managers = new ArrayList<>();

    public static void init() {
        register(ClientEventManager.INSTANCE);
        register(PlayerPacketManager.INSTANCE);
        register(TotemPopManager.INSTANCE);
    }

    private static void register(Manager manager) {
        managers.add(manager);
        RaptorClient.EVENT_BUS.subscribe(manager);
        MinecraftForge.EVENT_BUS.register(manager);
    }
}