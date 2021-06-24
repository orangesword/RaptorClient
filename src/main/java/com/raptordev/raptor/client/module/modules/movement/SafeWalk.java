package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.event.events.PlayerMoveEvent;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

@Module.Declaration(name = "SafeWalk", category = Category.Movement, Description = "Acts like you are sneaking")
public class SafeWalk extends Module {

    @EventHandler
    private final Listener<PlayerMoveEvent> moveEventListener = new Listener<>(event -> {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            if (SafeWalk.mc.player.onGround) {
                double increment = 0.05;
                while (x != 0.0 && this.isOffsetBBEmpty(x, -1.0, 0.0)) {
                    if (x < increment && x >= -increment) {
                        x = 0.0;
                        continue;
                    }
                    if (x > 0.0) {
                        x -= increment;
                        continue;
                    }
                    x += increment;
                }
                while (z != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, z)) {
                    if (z < increment && z >= -increment) {
                        z = 0.0;
                        continue;
                    }
                    if (z > 0.0) {
                        z -= increment;
                        continue;
                    }
                    z += increment;
                }
                while (x != 0.0 && z != 0.0 && this.isOffsetBBEmpty(x, -1.0, z)) {
                    x = x < increment && x >= -increment ? 0.0 : (x > 0.0 ? (x -= increment) : (x += increment));
                    if (z < increment && z >= -increment) {
                        z = 0.0;
                        continue;
                    }
                    if (z > 0.0) {
                        z -= increment;
                        continue;
                    }
                    z += increment;
                }
            }
            event.setX(x);
            event.setY(y);
            event.setZ(z);
    });

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        EntityPlayerSP playerSP = mc.player;
        return mc.world.getCollisionBoxes(playerSP, playerSP.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ)).isEmpty();
    }

}
