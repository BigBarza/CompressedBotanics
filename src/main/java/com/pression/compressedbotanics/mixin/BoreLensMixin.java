package com.pression.compressedbotanics.mixin;

import com.pression.compressedbotanics.CompressedBotanics;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.item.lens.BoreLens;

@Mixin(BoreLens.class)
public class BoreLensMixin {
    @Inject(method = "canHarvest", at = @At("HEAD"), remap = false, cancellable = true)
    private static void boreLensBlacklist(int harvestLevel, BlockState state, CallbackInfoReturnable<Boolean> cir){
        if(state.is(CompressedBotanics.BORE_LENS_NO_HARVEST)) cir.setReturnValue(false);
    }
}
