package com.raptordev.raptor.api.util.font;

import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.RaptorClient;
import net.minecraft.client.Minecraft;

public class FontUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float drawStringWithShadow(boolean customFont, String text, int x, int y, RCColor color) {
        if (customFont) {
            return RaptorClient.INSTANCE.cFontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
        }
    }

    public static int getStringWidth(boolean customFont, String string) {
        if (customFont) {
            return RaptorClient.INSTANCE.cFontRenderer.getStringWidth(string);
        } else {
            return mc.fontRenderer.getStringWidth(string);
        }
    }

    public static int getFontHeight(boolean customFont) {
        if (customFont) {
            return RaptorClient.INSTANCE.cFontRenderer.getHeight();
        } else {
            return mc.fontRenderer.FONT_HEIGHT;
        }
    }

    public static String trimStringToWidth(String substring, int width)
    {
        return mc.fontRenderer.trimStringToWidth(substring, width);
    }

    public static String trimStringToWidth(String text, int j, boolean b)
    {
        return mc.fontRenderer.trimStringToWidth(text, j, b);
    }

}