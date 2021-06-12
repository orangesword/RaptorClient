package com.raptordev.raptor.client.module.modules.render;

import com.raptordev.raptor.api.setting.values.ModeSetting;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

import java.util.Arrays;

@Module.Declaration(name = "Capes", category = Category.Render, drawn = false)
public class CapesModule extends Module {

    public ModeSetting capeMode = registerMode("Type", Arrays.asList("Black", "White"), "Black");
}