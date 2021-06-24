package com.raptordev.raptor.client.module.modules.client;

//import baritone.api.BaritoneAPI;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

@Module.Declaration(name = "Baritone", category = Category.CLIENT, alwaysEnabled = true)
public class Baritone extends Module {

    public BooleanSetting renderPath = registerBoolean("renderPath",  true);
    public ColorSetting pathColor = registerColor("pathColor",  new RCColor(255,255,255));
    public BooleanSetting renderGoal = registerBoolean("renderGoal",  true);
    public ColorSetting goalColor = registerColor("goalColor",  new RCColor(255,255,255));

    public BooleanSetting placeBlocks = registerBoolean("placeBlocks",  true);
    public BooleanSetting breakBlocks = registerBoolean("breakBlocks",  true);
    public BooleanSetting avoidDanger = registerBoolean("avoidDanger",  true);
    public BooleanSetting sprint = registerBoolean("sprint",  true);
    public BooleanSetting parkour = registerBoolean("parkour",  true);
    public BooleanSetting waterBucket = registerBoolean("waterBucket",  true);
    public BooleanSetting lava = registerBoolean("lava",  false);
    public BooleanSetting water = registerBoolean("water",  true);
    public BooleanSetting downward = registerBoolean("downward",  true);
    public BooleanSetting jumpAtBuildLimit = registerBoolean("jumpAtBuildLimit",  true);

    
//    public void onUpdate() {
//        BaritoneAPI.getSettings().renderPath.value = renderPath.getValue();
//        BaritoneAPI.getSettings().colorCurrentPath.value = new RCColor(pathColor.getValue());
//        BaritoneAPI.getSettings().renderGoal.value = renderGoal.getValue();
//        BaritoneAPI.getSettings().colorGoalBox.value = new RCColor(goalColor.getValue());
//
//        BaritoneAPI.getSettings().allowPlace.value = placeBlocks.getValue();
//        BaritoneAPI.getSettings().allowBreak.value = breakBlocks.getValue();
//        BaritoneAPI.getSettings().avoidance.value = avoidDanger.getValue();
//        BaritoneAPI.getSettings().allowSprint.value = sprint.getValue();
//        BaritoneAPI.getSettings().allowParkour.value = parkour.getValue();
//        BaritoneAPI.getSettings().allowWaterBucketFall.value = waterBucket.getValue();
//        BaritoneAPI.getSettings().assumeWalkOnLava.value = lava.getValue();
//        BaritoneAPI.getSettings().okIfWater.value = water.getValue();
//        BaritoneAPI.getSettings().allowDownward.value = downward.getValue();
//        BaritoneAPI.getSettings().allowJumpAt256.value = jumpAtBuildLimit.getValue();
//    }
}
