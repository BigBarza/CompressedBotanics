package com.pression.compressedbotanics.mixin.cocoon;

import com.pression.compressedbotanics.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.block_entity.CocoonBlockEntity;

@Mixin(CocoonBlockEntity.class)
public class CocoonChanceMixin {
    @Redirect(method = "hatch", at = @At(value = "INVOKE", target = "Ljava/lang/Math;random()D"), remap = false)
    private double setNewChance(){
        if(Math.random() < CommonConfig.COCOON_RARE_CHANCE.get()) return 0;
        else return 1;
    }
}
