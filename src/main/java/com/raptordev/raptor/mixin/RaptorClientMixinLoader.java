package com.raptordev.raptor.mixin;

import com.raptordev.raptor.client.RaptorClient;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name(RaptorClient.MODNAME)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class RaptorClientMixinLoader implements IFMLLoadingPlugin {

    private static boolean isObfuscatedEnvironment = false;

    public RaptorClientMixinLoader() {
        RaptorClient.log("Mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.raptor.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        RaptorClient.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}