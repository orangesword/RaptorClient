package com.raptordev.raptor.client.plugin;

import com.raptordev.raptor.api.util.font.FontUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PluginScreen extends GuiScreen {

    // Dont have any idea of how to do it. it will be done in next version

    private final String backgroundURL = "https://i.imgur.com/GCJRhiA.png";
    private final ResourceLocation resourceLocation = new ResourceLocation("raptor/background.png");
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(width, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public void initGui() {
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.x, this.y + 20, "Singleplayer"));
        this.buttonList.add(new GuiButton(1, this.x, this.y + 44, "Multiplayer"));
        this.buttonList.add(new GuiButton(2, this.x, this.y + 66, "Settings"));
        this.buttonList.add(new GuiButton(2, this.x, this.y + 88, "Exit"));
    }

    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (PluginScreen.isHovered(this.x - FontUtil.getStringWidth(false,"Singleplayer") / 2, this.y + 20, FontUtil.getStringWidth(false,"Singleplayer"), FontUtil.getFontHeight(false), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        } else if (PluginScreen.isHovered(this.x - FontUtil.getStringWidth(false,"Multiplayer") / 2, this.y + 44, FontUtil.getStringWidth(false,"Multiplayer"), FontUtil.getFontHeight(false), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (PluginScreen.isHovered(this.x - FontUtil.getStringWidth(false,"Settings") / 2, this.y + 66, FontUtil.getStringWidth(false,"Settings"), FontUtil.getFontHeight(false), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (PluginScreen.isHovered(this.x - FontUtil.getStringWidth(false,"Exit") / 2, this.y + 88, FontUtil.getStringWidth(false,"Exit"), FontUtil.getFontHeight(false), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.xOffset = -1.0f * (((float) mouseX - (float) this.width / 2.0f) / ((float) this.width / 32.0f));
        this.yOffset = -1.0f * (((float) mouseY - (float) this.height / 2.0f) / ((float) this.height / 18.0f));
        this.x = this.width / 2;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        PluginScreen.drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, this.width + 32, this.height + 18);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public BufferedImage parseBackground(BufferedImage background) {
        int height;
        int width = 1920;
        int srcWidth = background.getWidth();
        int srcHeight = background.getHeight();
        for (height = 1080; width < srcWidth || height < srcHeight; width *= 2, height *= 2) {
        }
        BufferedImage imgNew = new BufferedImage(width, height, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }

}
