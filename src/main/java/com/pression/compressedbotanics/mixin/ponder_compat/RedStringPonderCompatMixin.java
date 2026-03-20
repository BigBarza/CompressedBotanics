package com.pression.compressedbotanics.mixin.ponder_compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedbotanics.ClientConfig;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.render.block_entity.RedStringBlockEntityRenderer;
import vazkii.botania.common.block.block_entity.red_string.RedStringBlockEntity;

@Mixin(RedStringBlockEntityRenderer.class)
public class RedStringPonderCompatMixin {

    @Shadow(remap = false) private static int transparency;
    @Unique private static int heldValue = 0;

    @Inject(method = "render(Lvazkii/botania/common/block/block_entity/red_string/RedStringBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), remap = false)
    private void checkForPonderLevel(RedStringBlockEntity tile, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay, CallbackInfo ci){
        heldValue = transparency;
        if(ClientConfig.RED_STRING_IN_PONDERS.get() && tile.getLevel() instanceof PonderLevel ponder){
            transparency = 10;
        }
    }

    @Inject(method = "render(Lvazkii/botania/common/block/block_entity/red_string/RedStringBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("TAIL"), remap = false)
    private void resetTransparency(RedStringBlockEntity tile, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay, CallbackInfo ci){
        transparency = heldValue;
    }

}
