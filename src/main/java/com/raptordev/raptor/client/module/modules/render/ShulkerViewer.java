package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.font.FontUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.awt.*;

@Module.Declaration(name = "ShulkerViewer", category = Category.Render)
public class ShulkerViewer extends Module {

    public ColorSetting outlineColor = registerColor("Outline", new RCColor(255, 0, 0, 255));
    public ColorSetting fillColor = registerColor("Fill", new RCColor(0, 0, 0, 255));

    public void renderShulkerPreview(ItemStack itemStack, int posX, int posY, int width, int height) {
        RCColor outline = new RCColor(outlineColor.getValue(), 255);
        RCColor fill = new RCColor(fillColor.getValue(), 200);

        RenderUtil.draw2DRect(posX + 1, posY + 1, width - 2, height - 2, 1000, fill);

        RenderUtil.draw2DRect(posX, posY, width, 1, 1000, outline);
        RenderUtil.draw2DRect(posX, posY + height - 1, width, 1, 1000, outline);
        RenderUtil.draw2DRect(posX, posY, 1, height, 1000, outline);
        RenderUtil.draw2DRect(posX + width - 1, posY, 1, height, 1000, outline);

        GlStateManager.disableDepth();
        FontUtil.drawStringWithShadow(ModuleManager.getModule(ColorMain.class).customFont.getValue(), itemStack.getDisplayName(), posX + 3, posY + 3, new RCColor(255, 255, 255, 255));
        GlStateManager.enableDepth();

        NonNullList<ItemStack> contentItems = NonNullList.withSize(27, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(itemStack.getTagCompound().getCompoundTag("BlockEntityTag"), contentItems);

        for (int i = 0; i < contentItems.size(); i++) {
            int finalX = posX + 1 + i % 9 * 18;
            int finalY = posY + 31 + (i / 9 - 1) * 18;
            RaptorClientGui.renderItem(contentItems.get(i), new Point(finalX, finalY));
        }
    }
}