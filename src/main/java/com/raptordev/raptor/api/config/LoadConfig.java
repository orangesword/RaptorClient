package com.raptordev.raptor.api.config;

import com.raptordev.raptor.api.setting.Setting;
import com.raptordev.raptor.api.setting.SettingsManager;
import com.raptordev.raptor.api.util.font.CFontRenderer;
import com.raptordev.raptor.api.util.player.social.SocialManager;
import com.raptordev.raptor.client.RaptorClient;
import com.raptordev.raptor.api.clickgui.GuiConfig;
import com.raptordev.raptor.client.command.CommandManager;
import com.raptordev.raptor.client.module.Module;
import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.misc.AutoGG;
import com.raptordev.raptor.client.module.modules.misc.AutoReply;
import com.raptordev.raptor.client.module.modules.misc.AutoRespawn;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.raptordev.raptor.api.setting.values.*;
import com.raptordev.raptor.client.module.modules.misc.AutoSpam;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class LoadConfig {

    private static final String fileName = "RaptorClient/";
    private static final String moduleName = "Modules/";
    private static final String mainName = "Main/";
    private static final String miscName = "Misc/";

    public static void init() {
        try {
            loadModules();
            loadEnabledModules();
            loadModuleKeybinds();
            loadDrawnModules();
            loadToggleMessageModules();
            loadCommandPrefix();
            loadCustomFont();
            loadFriendsList();
            loadEnemiesList();
            loadClickGUIPositions();
            loadAutoGG();
            loadAutoReply();
            loadAutoRespawn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //big shoutout to lukflug for helping/fixing this
    private static void loadModules() throws IOException {
        String moduleLocation = fileName + moduleName;

        for (Module module : ModuleManager.getModules()) {
            try {
                loadModuleDirect(moduleLocation, module);
            } catch (IOException e) {
                System.out.println(module.getName());
                e.printStackTrace();
            }
        }
    }

    private static void loadModuleDirect(String moduleLocation, Module module) throws IOException {
        if (!Files.exists(Paths.get(moduleLocation + module.getName() + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(moduleLocation + module.getName() + ".json"));
        JsonObject moduleObject;
        try {
            moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
        }catch (java.lang.IllegalStateException e) {
            return;
        }

        if (moduleObject.get("Module") == null) {
            return;
        }

        JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
        for (Setting setting : SettingsManager.getSettingsForModule(module)) {
            JsonElement dataObject = settingObject.get(setting.getConfigName());
            try {
                if (dataObject != null && dataObject.isJsonPrimitive()) {
                    if (setting instanceof BooleanSetting) {
                        setting.setValue(dataObject.getAsBoolean());
                    } else if (setting instanceof IntegerSetting) {
                        setting.setValue(dataObject.getAsInt());
                    } else if (setting instanceof DoubleSetting) {
                        setting.setValue(dataObject.getAsDouble());
                    } else if (setting instanceof ColorSetting) {
                        ((ColorSetting) setting).fromInteger(dataObject.getAsInt());
                    } else if (setting instanceof ModeSetting) {
                        setting.setValue(dataObject.getAsString());
                    }
                }
            } catch (java.lang.NumberFormatException e) {
                System.out.println(setting.getConfigName() + " " + module.getName());
                System.out.println(dataObject);
            }
        }
        inputStream.close();
    }

    private static void loadEnabledModules() throws IOException {
        String enabledLocation = fileName + mainName;

        if (!Files.exists(Paths.get(enabledLocation + "Toggle" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "Toggle" + ".json"));
        JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) {
            return;
        }

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                if (dataObject.getAsBoolean()) {
                    try {
                        module.enable();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        inputStream.close();
    }

    private static void loadModuleKeybinds() throws IOException {
        String bindLocation = fileName + mainName;

        if (!Files.exists(Paths.get(bindLocation + "Bind" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(bindLocation + "Bind" + ".json"));
        JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) {
            return;
        }

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                module.setBind(dataObject.getAsInt());
            }
        }
        inputStream.close();
    }

    private static void loadDrawnModules() throws IOException {
        String drawnLocation = fileName + mainName;

        if (!Files.exists(Paths.get(drawnLocation + "Drawn" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(drawnLocation + "Drawn" + ".json"));
        JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) {
            return;
        }

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                module.setDrawn(dataObject.getAsBoolean());
            }
        }
        inputStream.close();
    }

    private static void loadToggleMessageModules() throws IOException {
        String toggleMessageLocation = fileName + mainName;

        if (!Files.exists(Paths.get(toggleMessageLocation + "ToggleMessages" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(toggleMessageLocation + "ToggleMessages" + ".json"));
        JsonObject moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) {
            return;
        }

        JsonObject toggleObject = moduleObject.get("Modules").getAsJsonObject();
        for (Module module : ModuleManager.getModules()) {
            JsonElement dataObject = toggleObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                module.setToggleMsg(dataObject.getAsBoolean());
            }
        }
        inputStream.close();
    }

    private static void loadCommandPrefix() throws IOException {
        String prefixLocation = fileName + mainName;

        if (!Files.exists(Paths.get(prefixLocation + "CommandPrefix" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(prefixLocation + "CommandPrefix" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Prefix") == null) {
            return;
        }

        JsonElement prefixObject = mainObject.get("Prefix");

        if (prefixObject != null && prefixObject.isJsonPrimitive()) {
            CommandManager.setCommandPrefix(prefixObject.getAsString());
        }
        inputStream.close();
    }

    private static void loadCustomFont() throws IOException {
        String fontLocation = fileName + miscName;

        if (!Files.exists(Paths.get(fontLocation + "CustomFont" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(fontLocation + "CustomFont" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Font Name") == null || mainObject.get("Font Size") == null) {
            return;
        }

        JsonElement fontNameObject = mainObject.get("Font Name");

        String name = null;

        if (fontNameObject != null && fontNameObject.isJsonPrimitive()) {
            name = fontNameObject.getAsString();
        }

        JsonElement fontSizeObject = mainObject.get("Font Size");

        int size = -1;

        if (fontSizeObject != null && fontSizeObject.isJsonPrimitive()) {
            size = fontSizeObject.getAsInt();
        }

        if (name != null && size != -1) {
            RaptorClient.INSTANCE.cFontRenderer = new CFontRenderer(new Font(name, Font.PLAIN, size), true, true);
            RaptorClient.INSTANCE.cFontRenderer.setFont(new Font(name, Font.PLAIN, size));
            RaptorClient.INSTANCE.cFontRenderer.setAntiAlias(true);
            RaptorClient.INSTANCE.cFontRenderer.setFractionalMetrics(true);
            RaptorClient.INSTANCE.cFontRenderer.setFontName(name);
            RaptorClient.INSTANCE.cFontRenderer.setFontSize(size);
        }
        inputStream.close();
    }

    private static void loadFriendsList() throws IOException {
        String friendLocation = fileName + miscName;

        if (!Files.exists(Paths.get(friendLocation + "Friends" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(friendLocation + "Friends" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Friends") == null) {
            return;
        }

        JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();

        friendObject.forEach(object -> SocialManager.addFriend(object.getAsString()));
        inputStream.close();
    }

    private static void loadEnemiesList() throws IOException {
        String enemyLocation = fileName + miscName;

        if (!Files.exists(Paths.get(enemyLocation + "Enemies" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(enemyLocation + "Enemies" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Enemies") == null) {
            return;
        }

        JsonArray enemyObject = mainObject.get("Enemies").getAsJsonArray();

        enemyObject.forEach(object -> SocialManager.addEnemy(object.getAsString()));
        inputStream.close();
    }

    private static void loadClickGUIPositions() throws IOException {
        RaptorClient.INSTANCE.raptorGUI.gui.loadConfig(new GuiConfig(fileName + mainName));
    }

    private static void loadAutoGG() throws IOException {
        String fileLocation = fileName + miscName;

        if (!Files.exists(Paths.get(fileLocation + "AutoGG" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoGG" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Messages") == null) {
            return;
        }
        JsonArray messageObject = mainObject.get("Messages").getAsJsonArray();

        messageObject.forEach(object -> AutoGG.addAutoGgMessage(object.getAsString()));
        inputStream.close();
    }

    private static void loadAutoSpam() throws IOException {
        String fileLocation = fileName + miscName;

        if (!Files.exists(Paths.get(fileLocation + "AutoSpam" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoSpam" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Messages") == null) {
            return;
        }
        JsonArray messageObject = mainObject.get("Messages").getAsJsonArray();

        messageObject.forEach(object -> AutoSpam.spamMessages.add(object.getAsString()));
        inputStream.close();
    }

    private static void loadAutoReply() throws IOException {
        String fileLocation = fileName + miscName;

        if (!Files.exists(Paths.get(fileLocation + "AutoReply" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoReply" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("AutoReply") == null) {
            return;
        }

        JsonObject arObject = mainObject.get("AutoReply").getAsJsonObject();
        JsonElement dataObject = arObject.get("Message");
        if (dataObject != null && dataObject.isJsonPrimitive()) {
            AutoReply.setReply(dataObject.getAsString());
        }
        inputStream.close();
    }

    private static void loadAutoRespawn() throws IOException {
        String fileLocation = fileName + miscName;

        if (!Files.exists(Paths.get(fileLocation + "AutoRespawn" + ".json"))) {
            return;
        }

        InputStream inputStream = Files.newInputStream(Paths.get(fileLocation + "AutoRespawn" + ".json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Message") == null) {
            return;
        }

        JsonElement dataObject = mainObject.get("Message");
        if (dataObject != null && dataObject.isJsonPrimitive()) {
            AutoRespawn.setAutoRespawnMessage(dataObject.getAsString());
        }

        inputStream.close();
    }
}