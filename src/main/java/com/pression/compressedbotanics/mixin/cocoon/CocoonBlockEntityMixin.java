package com.pression.compressedbotanics.mixin.cocoon;

import com.pression.compressedbotanics.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.block_entity.CocoonBlockEntity;

@Mixin(CocoonBlockEntity.class)
public class CocoonBlockEntityMixin {

    //This rolls against the chance defined in config and spoofs the roll's result accordingly to signal success or failure.
    @Redirect(method = "hatch", at = @At(value = "INVOKE", target = "Ljava/lang/Math;random()D"), remap = false)
    private double setNewChance(){
        if(Math.random() < CommonConfig.COCOON_RARE_CHANCE.get()) return 0;
        else return 1;
    }


    //This overwrites the time check with one based on the config value
    @Inject(method = "commonTick", at = @At("HEAD"), remap = false, cancellable = true)
    private static void tickNewTimer(Level level, BlockPos worldPosition, BlockState state, CocoonBlockEntity self, CallbackInfo ci){
        //We are skipping the original increment, so must do it here.
        self.timePassed++;
        //Can't call hatch here without making accessors.
        //So instead, set timePassed to a value beyond 2400 and NOT cancel.
        if(self.timePassed >= CommonConfig.COCOON_HATCH_TIME.get()){
            self.timePassed = 9999;
        } else ci.cancel();
    }

}
