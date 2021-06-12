package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.util.player.InventoryUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

/**
 * @author Hoosiers
 * @since 04/08/2021
 */
@Module.Declaration(name = "MCP", category = Category.Misc)
public class MCP extends Module {

    public void onUpdate() {
        RayTraceResult.Type type = mc.objectMouseOver.typeOfHit;

        if (type.equals(RayTraceResult.Type.MISS) && Mouse.isButtonDown(2)) {
            int oldSlot = mc.player.inventory.currentItem;

            int pearlSlot = InventoryUtil.findFirstItemSlot(ItemEnderPearl.class, 0, 8);

            if (pearlSlot != -1) {

                mc.player.inventory.currentItem = pearlSlot;
                mc.rightClickMouse();
                mc.player.inventory.currentItem = oldSlot;
            }
        }
    }
}