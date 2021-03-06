package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.lukflug.panelstudio.Context;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDComponent;
import com.lukflug.panelstudio.theme.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;

@Module.Declaration(name = "InventoryViewer", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = 0, posZ = 10)
public class InventoryViewer extends HUDModule {

    ColorSetting fillColor = registerColor("Fill", new RCColor(0, 0, 0, 100));
    ColorSetting outlineColor = registerColor("Outline", new RCColor(255, 0, 0, 255));

    @Override
    public void populate(Theme theme) {
        component = new InventoryViewerComponent(theme);
    }

    private class InventoryViewerComponent extends HUDComponent {

        public InventoryViewerComponent(Theme theme) {
            super(getName(), theme.getPanelRenderer(), InventoryViewer.this.position);
        }

        @Override
        public void render(Context context) {
            super.render(context);
            // Render background
            Color bgcolor = new RCColor(fillColor.getValue(), 100);
            context.getInterface().fillRect(context.getRect(), bgcolor, bgcolor, bgcolor, bgcolor);
            // Render outline
            Color color = outlineColor.getValue();
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(context.getSize().width, 1)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(context.getPos(), new Dimension(1, context.getSize().height)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x + context.getSize().width - 1, context.getPos().y), new Dimension(1, context.getSize().height)), color, color, color, color);
            context.getInterface().fillRect(new Rectangle(new Point(context.getPos().x, context.getPos().y + context.getSize().height - 1), new Dimension(context.getSize().width, 1)), color, color, color, color);
            // Render the actual items
            NonNullList<ItemStack> items = Minecraft.getMinecraft().player.inventory.mainInventory;
            for (int size = items.size(), item = 9; item < size; ++item) {
                int slotX = context.getPos().x + item % 9 * 18;
                int slotY = context.getPos().y + 2 + (item / 9 - 1) * 18;
                RaptorClientGui.renderItem(items.get(item), new Point(slotX, slotY));
            }
        }

        @Override
        public int getWidth(Interface inter) {
            return 162;
        }

        @Override
        public void getHeight(Context context) {
            context.setHeight(56);
        }
    }
}