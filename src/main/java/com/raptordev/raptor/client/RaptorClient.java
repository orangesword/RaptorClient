package com.raptordev.raptor.client;

import com.raptordev.raptor.api.config.LoadConfig;
import com.raptordev.raptor.api.manager.managers.DiscordManager;
import com.raptordev.raptor.api.setting.SettingsManager;
import com.raptordev.raptor.api.util.font.CFontRenderer;
import com.raptordev.raptor.api.util.misc.VersionChecker;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.api.util.render.CapeUtil;
import com.raptordev.raptor.api.clickgui.RaptorClientGui;
import com.raptordev.raptor.client.command.CommandManager;
import com.raptordev.raptor.api.manager.ManagerLoader;
import com.raptordev.raptor.client.module.ModuleManager;

import com.raptordev.raptor.client.module.modules.client.BetterConfig;
import com.raptordev.raptor.client.module.modules.client.ClickGuiModule;
import com.raptordev.raptor.client.module.modules.misc.AutoSpam;
import com.raptordev.raptor.client.module.modules.movement.Scaffold;
import com.raptordev.raptor.client.module.modules.render.Freecam;
import com.raptordev.raptor.client.plugin.PluginScreen;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;

@Mod(modid = RaptorClient.MODID, name = RaptorClient.MODNAME, version = RaptorClient.MODVER)
public class RaptorClient {

    public static final String MODNAME = "RaptorClient";
    public static final String MODID = "raptor";
    public static final String MODVER = "v1.1";

    public static final Logger LOGGER = LogManager.getLogger(MODNAME);
    public static final EventBus EVENT_BUS = new EventManager();

    @Mod.Instance
    public static RaptorClient INSTANCE;

    public RaptorClient() {
        INSTANCE = this;
    }

    public static void log(String info) {
        LOGGER.info(info);
    }

    public static void error(String error) {
        LOGGER.error(error);
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        Display.setTitle("RaptorClient" + " " + MODVER);
        cFontRenderer = new CFontRenderer(new Font("Verdana", Font.BOLD, 18), true, true);
        log("Custom font initialized!");
        VersionChecker.init();
        log("Version checked!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        log("Starting up " + "RaptorClient" + " " + MODVER + "!");

        startClient();
        log("RaptorClient" + " " + MODVER + "Started" + "!");

    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        log("Checking Modules");
        checkmodules();
        log("Modules Checked");
    }

    public CFontRenderer cFontRenderer;
    public RaptorClientGui raptorGUI;

    private void startClient() {

        SettingsManager.init();
        log("Settings initialized!");

        SocialManager.init();
        log("Friends/Enemies initialized!");

        ModuleManager.init();
        log("Modules initialized!");

        CommandManager.init();
        log("Commands initialized!");

        ManagerLoader.init();
        log("Managers initialized!");

        raptorGUI = new RaptorClientGui();
        log("GUI initialized!");

        CapeUtil.init();
        log("Capes initialized!");

        LoadConfig.init();
        log("Config initialized!");

        DiscordManager.startRPC();
        log("RPC Started");

    }

    private void checkmodules() {
        if (ModuleManager.getModule(Freecam.class).isEnabled()){
            ModuleManager.getModule(Freecam.class).disable();
            log("Detected FreeCam");}

        if (ModuleManager.getModule(AutoSpam.class).isEnabled()) {
            ModuleManager.getModule(AutoSpam.class).disable();
            log("Detected AntiSpam");}

        if (ModuleManager.getModule(ClickGuiModule.class).isEnabled()) {
            ModuleManager.getModule(ClickGuiModule.class).disable();
            log("Detected clickgui");}
        if (ModuleManager.getModule(ClickGuiModule.class).isToggleMsg())
            ModuleManager.getModule(ClickGuiModule.class).setToggleMsg(false);

        if (ModuleManager.getModule(Scaffold.class).isEnabled()) {
            ModuleManager.getModule(Scaffold.class).disable();
            log("Detected Scaffold");
        }

        ModuleManager.getModules().forEach(Module -> {
            if (Module.isAlwaysEnabled()) {
                Module.enable();
            }
        });

    }

}