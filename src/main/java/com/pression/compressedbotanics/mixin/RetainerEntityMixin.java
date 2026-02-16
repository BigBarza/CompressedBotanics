package com.pression.compressedbotanics.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.pression.compressedbotanics.mixin_interface.IRetainerMixin;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;
import vazkii.botania.common.block.block_entity.corporea.CorporeaRetainerBlockEntity;

@Mixin(CorporeaRetainerBlockEntity.class)
public class RetainerEntityMixin implements IRetainerMixin {
    @Shadow(remap = false)
    private boolean retainMissing;
    @Unique
    private boolean compressedBotanics$gotChanged = false;

    @Inject(method = "remember", at = @At("HEAD"), remap = false)
    private void rememberChanged(BlockPos pos, CorporeaRequestMatcher request, int count, int missing, CallbackInfo ci) {
        compressedBotanics$gotChanged = retainMissing; // only care about remembering if in "retain missing" mode
    }

    @Inject(method = "fulfilRequest", at = @At("HEAD"), remap = false)
    private void resetChanged(CallbackInfo ci) {
        compressedBotanics$gotChanged = false;
    }

    @WrapWithCondition(method = "fulfilRequest", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/block/block_entity/corporea/CorporeaRetainerBlockEntity;forget()V", remap = false), remap = false)
    private boolean doForget(CorporeaRetainerBlockEntity instance) {
        return !((IRetainerMixin) instance).compressedBotanics$getChanged();
    }

    @Override
    public boolean compressedBotanics$getChanged() {
        return compressedBotanics$gotChanged;
    }
}
