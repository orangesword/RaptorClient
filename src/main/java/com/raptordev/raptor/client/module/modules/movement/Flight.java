package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.Phase;
import com.raptordev.raptor.api.event.events.OnUpdateWalkingPlayerEvent;
import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.event.events.PlayerTravelEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.MathUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Arrays;

@Module.Declaration(name = "Flight", category = Category.Movement, Description = "Fly in any server")
public class Flight extends Module {

    public ModeSetting mode = registerMode("Mode", Arrays.asList("Vanilla", "Creative"), "Vanilla");
    public BooleanSetting Glide = registerBoolean("Glide", false);
    public BooleanSetting GlideWhileMoving = registerBoolean("GlideWhileMoving", false);
    public BooleanSetting ElytraOnly = registerBoolean("ElytraOnly", false);
    public BooleanSetting AntiFallDmg = registerBoolean("AntiFallDamage", false);
    public BooleanSetting AntiKick = registerBoolean("AntiKick", false);
    public DoubleSetting GlideSpeed = registerDouble("GlideSpeed",1,0,10);
    public DoubleSetting Speed = registerDouble("Speed",1,0,10);

    @EventHandler
    private Listener<PlayerTravelEvent> OnTravel = new Listener<>(p_Event ->
    {
        if (mc.player == null)
            return;

        if (ElytraOnly.getValue() && !mc.player.isElytraFlying())
            return;

        if (mode.getValue().equals("Creative"))
        {
            mc.player.setVelocity(0, 0, 0);

            final double[] dir = MathUtil.directionSpeed(Speed.getValue());

            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
            {
                mc.player.motionX = dir[0];
                mc.player.motionZ = dir[1];
            }

            if (mc.player.movementInput.jump && !mc.player.isElytraFlying())
                mc.player.motionY = Speed.getValue();

            if (mc.player.movementInput.sneak)
                mc.player.motionY = -Speed.getValue();

            if (Glide.getValue() && (GlideWhileMoving.getValue() ? (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0) : true))
            {
                mc.player.motionY += -GlideSpeed.getValue();
            }

            p_Event.cancel();

            mc.player.prevLimbSwingAmount = 0;
            mc.player.limbSwingAmount = 0;
            mc.player.limbSwing = 0;
        }
    });

    @EventHandler
    private Listener<OnUpdateWalkingPlayerEvent> OnPlayerUpdate = new Listener<>(p_Event ->
    {
        if (p_Event.getPhase() != Phase.PRE)
            return;

        if (ElytraOnly.getValue() && !mc.player.isElytraFlying())
            return;

        if (mode.getValue().equals("Vanilla"))
        {
            mc.player.setVelocity(0, 0, 0);

            final double[] dir = MathUtil.directionSpeed(Speed.getValue());

            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
            {
                mc.player.motionX = dir[0];
                mc.player.motionZ = dir[1];
            }

            if (mc.gameSettings.keyBindJump.isKeyDown())
                mc.player.motionY = Speed.getValue();

            if (mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = -Speed.getValue();
        }

        if (AntiKick.getValue() && (mc.player.ticksExisted % 4) == 0)
            mc.player.motionY += -0.04;
    });

    @EventHandler
    private Listener<PacketEvent> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.getPacket() instanceof CPacketPlayer)
        {
            if (!AntiFallDmg.getValue())
                return;

            if (mc.player.isElytraFlying())
                return;

            final CPacketPlayer l_Packet = (CPacketPlayer) p_Event.getPacket();

            if (mc.player.fallDistance > 3.8f)
            {
                l_Packet.onGround = true;
                mc.player.fallDistance = 0.0f;
            }
        }
    });

}
