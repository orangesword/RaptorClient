package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.EntitySteerEvent;
import com.raptordev.raptor.api.event.events.HorseSaddledEvent;
import com.raptordev.raptor.api.event.events.OnUpdateWalkingPlayerEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityLlama;

@Module.Declaration(name = "EntityControl", category = Category.Movement, Description = "Control rideable entity without saddle")
public class EntityControl extends Module{

    public BooleanSetting controlspeed = registerBoolean("ControlSpeed", false);
    public DoubleSetting Speed = registerDouble("Speed", 1,0.1,5);

    @EventHandler
    private final Listener<EntitySteerEvent> eventListener = new Listener<>(event -> {
        event.cancel();
    });

    @EventHandler
    private final Listener<HorseSaddledEvent> horseeventListener = new Listener<>(event -> {
        event.cancel();
    });

    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> listener = new Listener<>(event -> {
        if (mc.player == null || mc.player.getRidingEntity() == null) return;

        if (controlspeed.getValue()) {
            Entity ridingEntity = mc.player.getRidingEntity();

            if (ridingEntity instanceof EntityLlama) {
                ridingEntity.rotationYaw = mc.player.rotationYaw;
                ((EntityLlama) ridingEntity).rotationYawHead = mc.player.rotationYawHead;
            }

            double forward = mc.player.movementInput.moveForward;
            double strafe = mc.player.movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;

            if (forward == 0.0 && strafe == 0.0) {
                ridingEntity.motionX = 0.0;
                ridingEntity.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }

                double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                ridingEntity.motionX = forward * Speed.getValue() * cos + strafe * Speed.getValue() * sin;
                ridingEntity.motionZ = forward * Speed.getValue() * sin - strafe * Speed.getValue() * cos;
                if (ridingEntity instanceof EntityMinecart) {
                    EntityMinecart entityMinecart = (EntityMinecart) ridingEntity;
                    entityMinecart.setVelocity(forward * Speed.getValue() * cos + strafe * Speed.getValue() * sin, entityMinecart.motionY, forward * Speed.getValue() * sin - strafe * Speed.getValue() * cos);
                }

            }
        }

    });

}
