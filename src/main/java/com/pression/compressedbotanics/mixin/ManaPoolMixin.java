package com.pression.compressedbotanics.mixin;

import com.pression.compressedbotanics.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;

@Mixin(ManaPoolBlockEntity.class)
public class ManaPoolMixin {

    @Shadow(remap = false)
    private int manaCap;

    @Inject(method = "initManaCapAndNetwork", at = @At("HEAD"), remap = false)
    private void tweakFabulousPool(CallbackInfo ci){
        ManaPoolBlockEntity self = (ManaPoolBlockEntity)(Object)this;
        if(manaCap == -1 && ((ManaPoolBlock) self.getBlockState().getBlock()).variant == ManaPoolBlock.Variant.FABULOUS){
            manaCap = CommonConfig.FABULOUS_POOL_CAPACITY.get();
        }
    }
}
