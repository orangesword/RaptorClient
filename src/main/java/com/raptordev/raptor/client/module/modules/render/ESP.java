package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.setting.values.IntegerSetting;
import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.client.ColorMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.*;

import java.util.Arrays;


@Module.Declaration(name = "ESP", category = Category.Render)
public class ESP extends Module {

    ColorSetting mainColor = registerColor("Color");
    IntegerSetting range = registerInteger("Range", 100, 10, 260);
    IntegerSetting width = registerInteger("Line Width", 2, 1, 5);
    ModeSetting playerESPMode = registerMode("Player", Arrays.asList("None", "Glowing", "Box", "Direction"), "Box");
    ModeSetting mobESPMode = registerMode("Mob", Arrays.asList("None", "Glowing", "Box", "Direction"), "Box");
    BooleanSetting entityRender = registerBoolean("Entity", false);
    BooleanSetting itemRender = registerBoolean("Item", true);
    BooleanSetting containerRender = registerBoolean("Container", false);
    BooleanSetting glowCrystals = registerBoolean("Glow Crystal", false);

    RCColor playerColor;
    RCColor mobColor;
    RCColor mainIntColor;
    RCColor containerColor;
    int opacityGradient;

    public void onWorldRender(RenderEvent event) {
        mc.world.loadedEntityList.stream().filter(entity -> entity != mc.player).filter(entity -> rangeEntityCheck(entity)).forEach(entity -> {
            defineEntityColors(entity);

            if ((!playerESPMode.getValue().equals("None")) && entity instanceof EntityPlayer) {

                if (!playerESPMode.getValue().equals("None")) {

                    if (playerESPMode.getValue().equals("Glowing")) {
                        entity.setGlowing(true);

                    } else if (entity.isGlowing()) {
                        entity.setGlowing(false);
                    } else {
                        switch (playerESPMode.getValue()) {
                            case "Direction":
                                RenderUtil.drawBoxWithDirection(entity.getEntityBoundingBox(), playerColor, entity.rotationYaw, width.getValue(), 0);
                                break;
                            case "Box":
                                RenderUtil.drawBoundingBox(entity.getEntityBoundingBox(), width.getValue(), playerColor);
                                break;
                        }
                    }
                }
            }

            if (!mobESPMode.getValue().equals("None")) {

                if (entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid) {

                    if (mobESPMode.getValue().equals("Glowing")) {
                        entity.setGlowing(true);

                    } else if (entity.isGlowing()) {
                        entity.setGlowing(false);
                    } else if (mobESPMode.getValue().equals("Direction")) {
                        RenderUtil.drawBoxWithDirection(entity.getEntityBoundingBox(), mobColor, entity.rotationYaw, width.getValue(), 0);
                    } else {
                        RenderUtil.drawBoundingBox(entity.getEntityBoundingBox(), width.getValue(), mobColor);
                    }
                }
            }


            if (itemRender.getValue() && entity instanceof EntityItem) {
                RenderUtil.drawBoundingBox(entity.getEntityBoundingBox(), width.getValue(), mainIntColor);
            }
            if (entityRender.getValue()) {
                if (entity instanceof EntityEnderPearl || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityEnderCrystal) {
                    RenderUtil.drawBoundingBox(entity.getEntityBoundingBox(), width.getValue(), mainIntColor);
                }
            }
            if (glowCrystals.getValue() && entity instanceof EntityEnderCrystal) {
                entity.setGlowing(true);
            }

            if (!glowCrystals.getValue() && entity instanceof EntityEnderCrystal && entity.isGlowing()) {
                entity.setGlowing(false);
            }
        });


        if (containerRender.getValue()) {
            mc.world.loadedTileEntityList.stream().filter(tileEntity -> rangeTileCheck(tileEntity)).forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityChest) {
                    containerColor = new RCColor(255, 255, 0, opacityGradient);
                    RenderUtil.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), width.getValue(), containerColor);
                }
                if (tileEntity instanceof TileEntityEnderChest) {
                    containerColor = new RCColor(180, 70, 200, opacityGradient);
                    RenderUtil.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), width.getValue(), containerColor);
                }
                if (tileEntity instanceof TileEntityShulkerBox) {
                    containerColor = new RCColor(255, 0, 0, opacityGradient);
                    RenderUtil.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), width.getValue(), containerColor);
                }
                if (tileEntity instanceof TileEntityDispenser || tileEntity instanceof TileEntityFurnace || tileEntity instanceof TileEntityHopper || tileEntity instanceof TileEntityDropper) {
                    containerColor = new RCColor(150, 150, 150, opacityGradient);
                    RenderUtil.drawBoundingBox(mc.world.getBlockState(tileEntity.getPos()).getSelectedBoundingBox(mc.world, tileEntity.getPos()), width.getValue(), containerColor);
                }
            });
        }
    }

    public void onDisable() {
        mc.world.loadedEntityList.stream().forEach(entity -> {
            if ((entity instanceof EntityEnderCrystal || entity instanceof EntityPlayer || entity instanceof EntityCreature || entity instanceof EntitySlime || entity instanceof EntitySquid) && entity.isGlowing()) {
                entity.setGlowing(false);
            }
        });
    }

    private void defineEntityColors(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (SocialManager.isFriend(entity.getName())) {
                playerColor = ModuleManager.getModule(ColorMain.class).getFriendRCColor();
            } else if (SocialManager.isEnemy(entity.getName())) {
                playerColor = ModuleManager.getModule(ColorMain.class).getEnemyRCColor();
            } else {
                playerColor = new RCColor(mainColor.getValue(), opacityGradient);
            }
        }

        if (entity instanceof EntityMob) {
            mobColor = new RCColor(255, 0, 0, opacityGradient);
        } else if (entity instanceof EntityAnimal || entity instanceof EntitySquid) {
            mobColor = new RCColor(0, 255, 0, opacityGradient);
        } else {
            mobColor = new RCColor(255, 165, 0, opacityGradient);
        }

        if (entity instanceof EntitySlime) {
            mobColor = new RCColor(255, 0, 0, opacityGradient);
        }

        if (entity != null) {
            mainIntColor = new RCColor(mainColor.getValue(), opacityGradient);
        }
    }

    private boolean rangeEntityCheck(Entity entity) {
        if (entity.getDistance(mc.player) > range.getValue()) {
            return false;
        }

        if (entity.getDistance(mc.player) >= 180) {
            opacityGradient = 50;
        } else if (entity.getDistance(mc.player) >= 130 && entity.getDistance(mc.player) < 180) {
            opacityGradient = 100;
        } else if (entity.getDistance(mc.player) >= 80 && entity.getDistance(mc.player) < 130) {
            opacityGradient = 150;
        } else if (entity.getDistance(mc.player) >= 30 && entity.getDistance(mc.player) < 80) {
            opacityGradient = 200;
        } else {
            opacityGradient = 255;
        }

        return true;
    }

    private boolean rangeTileCheck(TileEntity tileEntity) {
        if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) > range.getValue() * range.getValue()) {
            return false;
        }

        if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 32400) {
            opacityGradient = 50;
        } else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 16900 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 32400) {
            opacityGradient = 100;
        } else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 6400 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 16900) {
            opacityGradient = 150;
        } else if (tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) >= 900 && tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) < 6400) {
            opacityGradient = 200;
        } else {
            opacityGradient = 255;
        }

        return true;
    }
}