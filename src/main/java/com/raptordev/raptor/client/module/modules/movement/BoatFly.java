package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.Comparator;

@Module.Declaration(name = "BoatFly", category = Category.Movement, Description = "Fly automatically with bots")
public class BoatFly extends Module {

    BooleanSetting bypass = registerBoolean("Bypass", true);
    BooleanSetting fixyaw = registerBoolean("FixYaw", false);
    BooleanSetting noGravity = registerBoolean("NoGravity", true);
    BooleanSetting placebypass = registerBoolean("PlaceBypass", false);
    IntegerSetting speed = registerInteger("Speed", 1,1,10);
    IntegerSetting Upspeed = registerInteger("UpSpeed", 1,1,10);
    private int packetCounter;
    private boolean stopFlying;

    protected void onEnable() {
        packetCounter = 0;
        stopFlying = false;
    }

    protected void onDisable() {
        packetCounter = 0;
        stopFlying = true;
    }

    public void onUpdate() {
        if (mc.player.getRidingEntity() instanceof EntityBoat) {
            this.steerBoat(mc.player.getRidingEntity());
        }
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetsend = new Listener<>(event -> {
        if (mc.player != null && mc.world != null) {
            if (this.mc.player.getRidingEntity() instanceof EntityBoat) {
                if (this.bypass.getValue() && event.getPacket() instanceof CPacketInput && !this.mc.gameSettings.keyBindSneak.isKeyDown() && !this.mc.player.getRidingEntity().onGround) {
                    ++this.packetCounter;
                    if (this.packetCounter == 3) {
                        this.NCPPacketTrick();
                    }
                }
                if ((this.bypass.getValue() && event.getPacket() instanceof SPacketPlayerPosLook) || (event.getPacket() instanceof SPacketMoveVehicle && !this.stopFlying)) {
                    event.cancel();
                }
            }
            if ((this.placebypass.getValue() && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBoat) || this.mc.player.getHeldItemOffhand().getItem() instanceof ItemBoat) {
                event.cancel();
            }
        }
    });

    private void steerBoat(final Entity boat) {
        if (this.fixyaw.getValue()) {
            boat.rotationYaw = this.mc.player.rotationYaw;
            boat.rotationPitch = this.mc.player.rotationPitch;
        }
        boat.setNoGravity(this.noGravity.getValue());
        int angle = 0;
        final boolean forward = this.mc.gameSettings.keyBindForward.isKeyDown();
        final boolean left = this.mc.gameSettings.keyBindLeft.isKeyDown();
        final boolean right = this.mc.gameSettings.keyBindRight.isKeyDown();
        final boolean back = this.mc.gameSettings.keyBindBack.isKeyDown();
        if (!forward || !back) {
            boat.motionY = 0.0;
        }
        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            boat.motionY += this.Upspeed.getValue() / 2.0f;
        }
        if (this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            boat.motionY -= this.Upspeed.getValue() / 2.0f;
        }
        if (!forward && !left && !right && !back) {
            return;
        }
        if (left && right) {
            if (forward) {
                angle = 0;
            }
            else if (back) {
                angle = 180;
            }
        }
        else if (forward && back) {
            if (left) {
                angle = -90;
            }
            else if (right) {
                angle = 90;
            }
            else {
                angle = -1;
            }
        }
        else if (left) {
            angle = -90;
        }
        else if (right) {
            angle = 90;
        }
        else {
            angle = 0;
        }
        if (forward) {
            angle /= 2;
        }
        else if (back) {
            angle = 180 - angle / 2;
        }
        if (angle == -1) {
            return;
        }
        if (this.isBorderingChunk(boat, boat.motionX, boat.motionZ)) {
            this.stopFlying = true;
        }
        else {
            this.stopFlying = false;
        }
        final float yaw = this.mc.player.rotationYaw + angle;
        boat.motionX = this.getRelativeX(yaw) * this.speed.getValue();
        boat.motionZ = this.getRelativeZ(yaw) * this.speed.getValue();
    }

    private double getRelativeX(final float yaw) {
        return Math.sin(Math.toRadians(-yaw));
    }

    private double getRelativeZ(final float yaw) {
        return Math.cos(Math.toRadians(yaw));
    }

    private boolean isBorderingChunk(final Entity boat, final Double motX, final Double motZ) {
        return this.mc.world.getChunk((int)(boat.posX + motX) / 16, (int)(boat.posZ + motZ) / 16) instanceof EmptyChunk;
    }

    private void NCPPacketTrick() {
        this.packetCounter = 0;
        this.mc.player.getRidingEntity().dismountRidingEntity();
        final Entity l_Entity = (Entity)this.mc.world.loadedEntityList.stream().filter(p_Entity -> p_Entity instanceof EntityBoat).min(Comparator.comparing(p_Entity -> this.mc.player.getDistance(p_Entity))).orElse(null);
        if (l_Entity != null) {
            this.mc.playerController.interactWithEntity((EntityPlayer)this.mc.player, l_Entity, EnumHand.MAIN_HAND);
        }
    }

}
