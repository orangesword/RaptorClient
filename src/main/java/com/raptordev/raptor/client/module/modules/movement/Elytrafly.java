package com.raptordev.raptor.client.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.event.events.PlayerTravelEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.MathUtil;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Arrays;

@Module.Declaration(name ="Elytrafly", category = Category.Movement, Description = "Fly with elytra automatically")
public class Elytrafly extends Module {

    public ModeSetting mode = registerMode("Mode", Arrays.asList("Normal", "Tarzan", "Superior", "Packet", "Control"),"Normal");
    public DoubleSetting speed = registerDouble("Speed", 1.82, 0.0, 10.0);
    public DoubleSetting DownSpeed = registerDouble("DownSpeed", 1.82, 0.0, 10.0);
    public DoubleSetting GlideSpeed = registerDouble("GlideSpeed", 1, 0, 10);
    public DoubleSetting UpSpeed = registerDouble("UpSpeed", 2.0, 0, 10);
    public BooleanSetting Accelerate = registerBoolean("Accelerate", true);
    public IntegerSetting vAccelerationTimer = registerInteger("Timer",1000, 0, 10000);
    public DoubleSetting RotationPitch = registerDouble("RotationPitch", 10, -90, 90);
    public BooleanSetting CancelInWater = registerBoolean("CancelInWater", true);
    public IntegerSetting CancelAtHeight = registerInteger("CancelAtHeight", 5, 0, 10);
    public BooleanSetting InstantFly = registerBoolean("InstantFly", true);
    public BooleanSetting EquipElytra = registerBoolean("EquipElytra", false);
    public BooleanSetting PitchSpoof = registerBoolean("PitchSpoof", false);

    private Timer PacketTimer = new Timer();
    private Timer AccelerationTimer = new Timer();
    private Timer AccelerationResetTimer = new Timer();
    private Timer InstantFlyTimer = new Timer();
    private boolean SendMessage = false;
    private Flight Flight = null;

    private int ElytraSlot = -1;

    @Override
    public void onEnable()
    {
        super.onEnable();

        Flight = ModuleManager.getModule(Flight.class);

        ElytraSlot = -1;

        if (EquipElytra.getValue())
        {
            if (mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            {
                for (int l_I = 0; l_I < 44; ++l_I)
                {
                    ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);

                    if (l_Stack.isEmpty() || l_Stack.getItem() != Items.ELYTRA)
                        continue;

                    ItemElytra l_Elytra = (ItemElytra)l_Stack.getItem();

                    ElytraSlot = l_I;
                    break;
                }

                if (ElytraSlot != -1)
                {
                    boolean l_HasArmorAtChest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;

                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);

                    if (l_HasArmorAtChest)
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    @Override
    public void onDisable()
    {
        super.onDisable();

        if (mc.player == null)
            return;

        if (ElytraSlot != -1)
        {
            boolean l_HasItem = !mc.player.inventory.getStackInSlot(ElytraSlot).isEmpty() || mc.player.inventory.getStackInSlot(ElytraSlot).getItem() != Items.AIR;

            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, ElytraSlot, 0, ClickType.PICKUP, mc.player);

            if (l_HasItem)
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
        }
    }


    @EventHandler
    private Listener<PlayerTravelEvent> OnTravel = new Listener<>(p_Event ->
    {
        if (mc.player == null || Flight.isEnabled()) ///< Ignore if Flight is on: ex flat flying
            return;

        /// Player must be wearing an elytra.
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            return;

        if (!mc.player.isElytraFlying())
        {
            if (!mc.player.onGround && InstantFly.getValue())
            {
                if (!InstantFlyTimer.hasPassedS(1000))
                    return;

                InstantFlyTimer.reset();

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_FALL_FLYING));
            }

            return;
        }

        switch (mode.getValue())
        {
            case "Normal":
            case "Tarzan":
            case "Packet":
                HandleNormalModeElytra(p_Event);
                break;
            case "Superior":
                HandleImmediateModeElytra(p_Event);
                break;
            case "Control":
                HandleControlMode(p_Event);
                break;
            default:
                break;
        }
    });

    public void HandleNormalModeElytra(PlayerTravelEvent p_Travel)
    {
        double l_YHeight = mc.player.posY;

        if (l_YHeight <= CancelAtHeight.getValue())
        {
            if (!SendMessage)
            {
                MessageBus.sendClientPrefixMessage(ChatFormatting.RED + "WARNING, you must scaffold up or use fireworks, as YHeight <= CancelAtHeight!");
                SendMessage = true;
            }

            return;
        }

        boolean l_IsMoveKeyDown = mc.player.movementInput.moveForward > 0 || mc.player.movementInput.moveStrafe > 0;

        boolean l_CancelInWater = !mc.player.isInWater() && !mc.player.isInLava() && CancelInWater.getValue();

        if (mc.player.movementInput.jump)
        {
            p_Travel.cancel();
            Accelerate();
            return;
        }

        if (!l_IsMoveKeyDown)
        {
            AccelerationTimer.resetTimeSkipTo(-vAccelerationTimer.getValue());
        }
        else if ((mc.player.rotationPitch <= RotationPitch.getValue() || mode.getValue().equals("Tarzan")) && l_CancelInWater)
        {
            if (Accelerate.getValue())
            {
                if (AccelerationTimer.hasPassedS(vAccelerationTimer.getValue()))
                {
                    Accelerate();
                    return;
                }
            }
            return;
        }

        p_Travel.cancel();
        Accelerate();
    }

    public void HandleImmediateModeElytra(PlayerTravelEvent p_Travel)
    {
        if (mc.player.movementInput.jump)
        {
            double l_MotionSq = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);

            if (l_MotionSq > 1.0)
            {
                return;
            }
            else
            {
                double[] dir = MathUtil.directionSpeedNoForward(speed.getValue());

                mc.player.motionX = dir[0];
                mc.player.motionY = -(GlideSpeed.getValue() / 10000f);
                mc.player.motionZ = dir[1];
            }

            p_Travel.cancel();
            return;
        }

        mc.player.setVelocity(0, 0, 0);

        p_Travel.cancel();

        double[] dir = MathUtil.directionSpeed(speed.getValue());

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionY = -(GlideSpeed.getValue() / 10000f);
            mc.player.motionZ = dir[1];
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.getValue();

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }

    public void Accelerate()
    {
        if (AccelerationResetTimer.hasPassedS(vAccelerationTimer.getValue()))
        {
            AccelerationResetTimer.reset();
            AccelerationTimer.reset();
            SendMessage = false;
        }

        float l_Speed = this.speed.getValue().floatValue();

        final double[] dir = MathUtil.directionSpeed(l_Speed);

        mc.player.motionY = -(GlideSpeed.getValue() / 10000f);

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        if (mc.player.movementInput.sneak)
            mc.player.motionY = -DownSpeed.getValue();

        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
    }


    private void HandleControlMode(PlayerTravelEvent p_Event)
    {
        final double[] dir = MathUtil.directionSpeed(speed.getValue());

        if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0)
        {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];

            mc.player.motionX -= (mc.player.motionX*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionX;
            mc.player.motionZ -= (mc.player.motionZ*(Math.abs(mc.player.rotationPitch)+90)/90) - mc.player.motionZ;
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }

        mc.player.motionY = (-MathUtil.degToRad(mc.player.rotationPitch)) * mc.player.movementInput.moveForward;


        mc.player.prevLimbSwingAmount = 0;
        mc.player.limbSwingAmount = 0;
        mc.player.limbSwing = 0;
        p_Event.cancel();
    }

    @EventHandler
    private Listener<PacketEvent> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.getPacket() instanceof CPacketPlayer && PitchSpoof.getValue())
        {
            if (!mc.player.isElytraFlying())
                return;

            if (p_Event.getPacket() instanceof CPacketPlayer.PositionRotation && PitchSpoof.getValue())
            {
                CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation) p_Event.getPacket();

                mc.getConnection().sendPacket(new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                p_Event.cancel();
            }
            else if (p_Event.getPacket() instanceof CPacketPlayer.Rotation && PitchSpoof.getValue())
            {
                p_Event.cancel();
            }
        }
    });
}
