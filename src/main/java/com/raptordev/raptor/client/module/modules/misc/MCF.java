package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

@Module.Declaration(name = "MCF", category = Category.Misc)
public class MCF extends Module {

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> listener = new Listener<>(event -> {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityPlayer && Mouse.isButtonDown(2)) {
            if (SocialManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
                SocialManager.delFriend(mc.objectMouseOver.entityHit.getName());
                MessageBus.sendClientPrefixMessage(ModuleManager.getModule(ColorMain.class).getDisabledColor() + "Removed " + mc.objectMouseOver.entityHit.getName() + " from friends list");
            } else {
                SocialManager.addFriend(mc.objectMouseOver.entityHit.getName());
                MessageBus.sendClientPrefixMessage(ModuleManager.getModule(ColorMain.class).getEnabledColor() + "Added " + mc.objectMouseOver.entityHit.getName() + " to friends list");
            }
        }
    });
}