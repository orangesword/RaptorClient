package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.util.font.FontUtil;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

@Module.Declaration(name = "ArmorHUD", category = Category.HUD,toggleMsg = false)
public class ArmorHUD extends Module {

    private static final RenderItem itemRender = mc.getRenderItem();

    public void onRender() {
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();

        ScaledResolution resolution = new ScaledResolution(mc);
        int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
        for (ItemStack is : mc.player.inventory.armorInventory) {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();

            itemRender.zLevel = 200F;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            itemRender.zLevel = 0F;

            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, new RCColor(255, 255, 255).getRGB());
            float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
            float red = 1 - green;
            int dmg = 100 - (int) (red * 100);

            if (green > 1) {
                green = 1;
            } else if (green < 0) {
                green = 0;
            }

            if (red > 1) {
                red = 1;
            }
            if (dmg < 0) {
                dmg = 0;
            }

            FontUtil.drawStringWithShadow(ModuleManager.getModule(ColorMain.class).customFont.getValue(), dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, new RCColor((int) (red * 255), (int) (green * 255), 0));
        }

        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}