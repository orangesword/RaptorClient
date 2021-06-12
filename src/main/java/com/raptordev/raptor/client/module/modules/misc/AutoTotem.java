package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.combat.OffHand;
import net.minecraft.client.gui.inventory.GuiContainer;

@Module.Declaration(name = "Auto Totem", category = Category.Misc, Description = "Use Totems Automatically")
public class AutoTotem extends Module {

    private OffHand offHand = ModuleManager.getModule(OffHand.class);

    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer) return;

            offHand.nonDefaultItem.setValue("Totem");
            offHand.defaultItem.setValue("Totem");
            offHand.noPlayerItem.setValue("Totem");

    }

    protected void onEnable() {

        offHand.enable();
        offHand.setToggleMsg(false);

    }

    protected void onDisable() {
        offHand.disable();
        offHand.setToggleMsg(offHand.isToggleMsg());

        offHand.nonDefaultItem.setValue(offHand.nonDefaultItem.getValue());
        offHand.defaultItem.setValue(offHand.defaultItem.getValue());
        offHand.noPlayerItem.setValue(offHand.noPlayerItem.getValue());

    }
}
