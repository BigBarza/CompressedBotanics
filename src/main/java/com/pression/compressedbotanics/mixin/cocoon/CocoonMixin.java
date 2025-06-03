package com.pression.compressedbotanics.mixin.cocoon;

import com.pression.compressedbotanics.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.CocoonBlock;
import vazkii.botania.common.item.BotaniaItems;

@Mixin(CocoonBlock.class)
public class CocoonMixin {
    @Inject(method = "addStack", at = @At("HEAD"), remap = false, cancellable = true)
    private void checkConfigs(Level world, BlockPos pos, ItemStack stack, boolean creative, CallbackInfoReturnable<InteractionResult> cir){
        if(CommonConfig.COCOON_NO_EMERALD.get() && stack.is(Items.EMERALD)) cir.setReturnValue(InteractionResult.PASS);
        if(CommonConfig.COCOON_NO_CHORUS.get() && stack.is(Items.CHORUS_FRUIT)) cir.setReturnValue(InteractionResult.PASS);
        if(CommonConfig.COCOON_NO_GAIA.get() && stack.is(BotaniaItems.lifeEssence)) cir.setReturnValue(InteractionResult.PASS);
    }
}
