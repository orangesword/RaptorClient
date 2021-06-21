package com.raptordev.raptor.mixin.mixins;

import com.raptordev.raptor.client.module.ModuleManager;
import com.raptordev.raptor.client.module.modules.movement.NoSlow;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo callbackInfo) {
        NoSlow module = ModuleManager.getModule(NoSlow.class);

        if (module.isEnabled()) {
            callbackInfo.cancel();
        }
    }
}