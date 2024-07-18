package com.pression.compressedbotanics.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity;

@Mixin(HydroangeasBlockEntity.class)
public class HydroangeasMixin { //This is just a small thing because i could not be arsed to mess with nbt to stop the hydroangeas' natural decay.
    @Shadow(remap = false) private int passiveDecayTicks;
    @Inject(method = "tickFlower", at = @At("TAIL"), remap = false)
    private void yeet(CallbackInfo ci){
        passiveDecayTicks = 0;
    }
}
