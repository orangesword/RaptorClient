package com.raptordev.raptor.client.module.modules.movement;

import com.raptordev.raptor.api.event.Phase;
import com.raptordev.raptor.api.event.events.OnUpdateWalkingPlayerEvent;
import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.event.events.PlayerMoveEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.api.util.player.PlayerUtil;
import com.raptordev.raptor.api.util.player.RotationUtil;
import com.raptordev.raptor.api.util.player.SpoofRotationUtil;
import com.raptordev.raptor.api.util.world.BlockInteractionHelper;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;

@Module.Declaration(name = "Scaffold", category = Category.Misc)
public class Scaffold extends Module {

    public ModeSetting Mode  = registerMode("Mode", Arrays.asList("Tower", "Normal"), "Tower");
    public BooleanSetting StopMotion = registerBoolean("StopMotion", false);
    public DoubleSetting Delay = registerDouble("Delay", 0.1,0, 1);

    private Timer _timer = new Timer();
    private Timer _towerPauseTimer = new Timer();
    private Timer _towerTimer = new Timer();

    @EventHandler
    private Listener<OnUpdateWalkingPlayerEvent> onMotionUpdate = new Listener<>(event ->
    {
        if (event.isCancelled())
            return;

        if (event.getPhase() != Phase.PRE)
            return;

        if (!_timer.hasPassedS(Delay.getValue().longValue()))
            return;

        // verify we have a block in our hand
        ItemStack stack = mc.player.getHeldItemMainhand();

        int prevSlot = -1;

        if (!verifyStack(stack))
        {
            for (int i = 0; i < 9; ++i)
            {
                stack = mc.player.inventory.getStackInSlot(i);

                if (verifyStack(stack))
                {
                    prevSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = i;
                    mc.playerController.updateController();
                    break;
                }
            }
        }

        if (!verifyStack(stack))
            return;

        _timer.reset();

        BlockPos toPlaceAt = null;

        BlockPos feetBlock = PlayerUtil.getPlayerPos().down();

        boolean placeAtFeet = isValidPlaceBlockState(feetBlock);

        // verify we are on tower mode, feet block is valid to be placed at, and
        if (Mode.getValue().equals("Tower") && placeAtFeet && mc.player.movementInput.jump && _towerTimer.getTimePassed() == 250 && !mc.player.isElytraFlying())
        {
            // todo: this can be moved to only do it on an SPacketPlayerPosLook?
            if (_towerPauseTimer.getTimePassed() == 1500)
            {
                _towerPauseTimer.reset();
                mc.player.motionY = -0.28f;
            }
            else
            {
                final float towerMotion = 0.41999998688f;

                mc.player.setVelocity(0, towerMotion, 0);

            }
        }

        if (placeAtFeet)
            toPlaceAt = feetBlock;
        else // find a supporting position for feet block
        {
            BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(feetBlock);

            // find a supporting block
            if (result != BlockInteractionHelper.ValidResult.Ok && result != BlockInteractionHelper.ValidResult.AlreadyBlockThere)
            {
                BlockPos[] array = { feetBlock.north(), feetBlock.south(), feetBlock.east(), feetBlock.west() };

                BlockPos toSelect = null;
                double lastDistance = 420.0;

                for (BlockPos pos : array)
                {
                    if (!isValidPlaceBlockState(pos))
                        continue;

                    double dist = pos.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ);
                    if (lastDistance > dist)
                    {
                        lastDistance = dist;
                        toSelect = pos;
                    }
                }

                // if we found a position, that's our selection
                if (toSelect != null)
                    toPlaceAt = toSelect;
            }

        }

        if (toPlaceAt != null)
        {
            // PositionRotation
            // CPacketPlayerTryUseItemOnBlock
            // CPacketAnimation

            final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);

            for (final EnumFacing side : EnumFacing.values())
            {
                final BlockPos neighbor = toPlaceAt.offset(side);
                final EnumFacing side2 = side.getOpposite();

                if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false))
                {
                    final Vec3d hitVec = new Vec3d((Vec3i) neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.distanceTo(hitVec) <= 5.0f)
                    {
                        float[] rotations = BlockInteractionHelper.getFacingRotations(toPlaceAt.getX(), toPlaceAt.getY(), toPlaceAt.getZ(), side);

                        event.cancel();
                        SpoofRotationUtil.ROTATION_UTIL.setYawAndPitch(rotations[1], rotations[0]);
                        break;
                    }
                }
            }

            if (BlockInteractionHelper.place(toPlaceAt, 5.0f, false, false, true) == BlockInteractionHelper.PlaceResult.Placed)
            {
                // swinging is already in the place function.
            }
        }
        else
            _towerPauseTimer.reset();

        // set back our previous slot
        if (prevSlot != -1)
        {
            mc.player.inventory.currentItem = prevSlot;
            mc.playerController.updateController();
        }
    });

    @EventHandler
    private Listener<PacketEvent> PacketEvent = new Listener<>(event ->
    {
        if (event.getPacket() instanceof SPacketPlayerPosLook)
        {
            // reset this if we flagged the anticheat
            _towerTimer.reset();
        }
    });

    @EventHandler
    private Listener<PlayerMoveEvent> OnPlayerMove = new Listener<>(p_Event ->
    {
        if (!StopMotion.getValue())
            return;

        double x = p_Event.getX();
        double y = p_Event.getY();
        double z = p_Event.getZ();

        if (mc.player.onGround && !mc.player.noClip)
        {
            double increment;
            for (increment = 0.05D; x != 0.0D && isOffsetBBEmpty(x, -1.0f, 0.0D);)
            {
                if (x < increment && x >= -increment)
                {
                    x = 0.0D;
                }
                else if (x > 0.0D)
                {
                    x -= increment;
                }
                else
                {
                    x += increment;
                }
            }
            for (; z != 0.0D && isOffsetBBEmpty(0.0D, -1.0f, z);)
            {
                if (z < increment && z >= -increment)
                {
                    z = 0.0D;
                }
                else if (z > 0.0D)
                {
                    z -= increment;
                }
                else
                {
                    z += increment;
                }
            }
            for (; x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -1.0f, z);)
            {
                if (x < increment && x >= -increment)
                {
                    x = 0.0D;
                }
                else if (x > 0.0D)
                {
                    x -= increment;
                }
                else
                {
                    x += increment;
                }
                if (z < increment && z >= -increment)
                {
                    z = 0.0D;
                }
                else if (z > 0.0D)
                {
                    z -= increment;
                }
                else
                {
                    z += increment;
                }
            }
        }

        p_Event.setX(x);
        p_Event.setY(y);
        p_Event.setZ(z);
        p_Event.cancel();
    });

    private boolean isOffsetBBEmpty(double x, double y, double z)
    {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(x, y, z)).isEmpty();
    }

    private boolean isValidPlaceBlockState(BlockPos pos)
    {
        BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(pos);

        if (result == BlockInteractionHelper.ValidResult.AlreadyBlockThere)
            return mc.world.getBlockState(pos).getMaterial().isReplaceable();

        return result == BlockInteractionHelper.ValidResult.Ok;
    }

    private boolean verifyStack(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemBlock;
    }

}
