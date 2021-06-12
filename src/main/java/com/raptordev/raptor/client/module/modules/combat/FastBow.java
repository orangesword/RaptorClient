package com.raptordev.raptor.client.module.modules.combat;

import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name = "FastBow", category = Category.Combat, Description = "Automatically releases a bow at minimum draw to spam arrows")
public class FastBow extends Module {

    IntegerSetting drawLength = registerInteger("Draw Length", 3, 3, 21);

    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= drawLength.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
            mc.player.stopActiveHand();
        }
    }
}