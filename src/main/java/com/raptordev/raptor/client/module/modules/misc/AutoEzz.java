package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Module.Declaration(name = "Autoezz", category = Category.Misc, Description = "Just like auto ezz but only for cool people")
public class AutoEzz extends Module {

    static List<String> AutoezzMessages = new ArrayList<>();
    private ConcurrentHashMap targetedPlayers = null;
    int index = -1;

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (mc.player != null) {
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap();
            }

            if (event.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.getPacket();
                if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                    Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
                    if (targetEntity instanceof EntityPlayer) {
                        this.addTargetedPlayer(targetEntity.getName());
                    }
                }
            }
        }
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<LivingDeathEvent> livingDeathEventListener = new Listener<>(event -> {
        if (mc.player != null) {
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap();
            }

            EntityLivingBase entity = event.getEntityLiving();
            if (entity != null) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (player.getHealth() <= 0.0F) {
                        String name = player.getName();
                        if (this.shouldAnnounce(name)) {
                            this.doAnnounce(name);
                        }
                    }
                }
            }
        }
    });

    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap();
    }

    public void onDisable() {
        this.targetedPlayers = null;
    }

    public void onUpdate() {
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap();
        }

        Iterator var1 = mc.world.getLoadedEntityList().iterator();

        while (var1.hasNext()) {
            Entity entity = (Entity) var1.next();
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getHealth() <= 0.0F) {
                    String name = player.getName();
                    if (this.shouldAnnounce(name)) {
                        this.doAnnounce(name);
                        break;
                    }
                }
            }
        }

        targetedPlayers.forEach((namex, timeout) -> {
            if ((int) timeout <= 0) {
                this.targetedPlayers.remove(namex);
            } else {
                this.targetedPlayers.put(namex, (int) timeout - 1);
            }

        });
    }

    private boolean shouldAnnounce(String name) {
        return this.targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {
        targetedPlayers.remove(name);
        if (index >= (AutoezzMessages.size() - 1)) index = -1;
        index++;
        String message;
        if (AutoezzMessages.size() > 0)
            message = AutoezzMessages.get(index);
        else
            message = "EZZZZZZ I think you are not using RaptorClient";

        String messageSanitized = message.replaceAll("ยง", "").replace("{name}", name);
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }

        MessageBus.sendServerMessage(messageSanitized);
    }

    public void addTargetedPlayer(String name) {
        if (!Objects.equals(name, mc.player.getName())) {
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ConcurrentHashMap();
            }

            targetedPlayers.put(name, 20);
        }
    }


    public static void addAutoezzMessage(String s) {
        AutoezzMessages.add(s);
    }

    public static List<String> getAutoezzMessages() {
        return AutoezzMessages;
    }
    
}
