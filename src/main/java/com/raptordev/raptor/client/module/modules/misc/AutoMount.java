package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.event.events.OnUpdateWalkingPlayerEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.DoubleSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.util.misc.Timer;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.*;
import net.minecraft.util.EnumHand;

import java.util.Comparator;

@Module.Declaration(name = "AutoMount", category = Category.Misc)
public class AutoMount extends Module {

    public BooleanSetting Boats = registerBoolean("Boats", false);
    public BooleanSetting Horses = registerBoolean("Horses",  true);
    public BooleanSetting SkeletonHorses = registerBoolean("SkeletonHorses", true);
    public BooleanSetting Donkeys = registerBoolean("Donkeys", true);
    public BooleanSetting Pigs = registerBoolean("Pigs", true);
    public BooleanSetting Llamas = registerBoolean("Llamas",  true);
    public BooleanSetting Mules = registerBoolean("Mules",  true);
    public IntegerSetting Range = registerInteger("Range",4, 0, 10);
    public DoubleSetting Delay = registerDouble("Delay",  1.0f, 0.0f, 10.0f);

    private Timer timer = new Timer();

    @EventHandler
    private Listener<OnUpdateWalkingPlayerEvent> OnPlayerUpdate = new Listener<>(p_Event ->
    {
        if (mc.player.isRiding())
            return;

        if (!timer.hasPassedS(Delay.getValue().longValue()))
            return;

        timer.reset();

        Entity l_Entity = mc.world.loadedEntityList.stream()
                .filter(p_Entity -> isValidEntity(p_Entity))
                .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                .orElse(null);

        if (l_Entity != null)
            mc.playerController.interactWithEntity(mc.player, l_Entity, EnumHand.MAIN_HAND);
    });

    private boolean isValidEntity(Entity entity)
    {
        if (entity.getDistance(mc.player) > Range.getValue())
            return false;

        if (entity instanceof AbstractHorse)
        {
            AbstractHorse horse = (AbstractHorse) entity;

            if (horse.isChild())
                return false;
        }

        if (entity instanceof EntityBoat && Boats.getValue())
            return true;

        if (entity instanceof EntitySkeletonHorse && SkeletonHorses.getValue())
            return true;

        if (entity instanceof EntityHorse && Horses.getValue())
            return true;

        if (entity instanceof EntityDonkey && Donkeys.getValue())
            return true;

        if (entity instanceof EntityPig && Pigs.getValue())
        {
            EntityPig pig = (EntityPig) entity;

            if (pig.getSaddled())
                return true;

            return false;
        }

        if (entity instanceof EntityLlama && Llamas.getValue())
        {
            EntityLlama l_Llama = (EntityLlama) entity;

            if (!l_Llama.isChild())
                return true;
        }

        if (entity instanceof EntityMule && Llamas.getValue())
        {
            EntityLlama l_Llama = (EntityLlama) entity;

            if (!l_Llama.isChild())
                return true;
        }

        return false;
    }

}
