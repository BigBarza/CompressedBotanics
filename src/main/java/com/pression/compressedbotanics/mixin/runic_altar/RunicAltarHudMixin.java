package com.pression.compressedbotanics.mixin.runic_altar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedbotanics.recipe.IRunicRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;

@Mixin(RunicAltarBlockEntity.Hud.class)
public class RunicAltarHudMixin {

    @Unique
    private static RunicAltarRecipe curRecipe = null;
    @Unique
    private static long timer = 0;

    @Inject(method = "lambda$render$0", at = @At("HEAD"), remap = false)
    private static void captureVariables(RunicAltarBlockEntity altar, PoseStack ms, int xc, int radius, int yc, Minecraft mc, RunicAltarRecipe recipe, CallbackInfo ci){
        curRecipe = recipe;
        if(altar.getLevel()!=null) timer = altar.getLevel().getGameTime();
    }

    @ModifyArg(method = "lambda$render$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V"), remap = false, index = 0)
    private static ItemLike renderCatalyst(ItemLike item){
        if(item == (BotaniaBlocks.livingrock)){
            Ingredient catalyst = ((IRunicRecipe) curRecipe).getCatalyst();
            if(catalyst == Ingredient.EMPTY) return Blocks.AIR.asItem();
            int adjustedTimer = (int) timer % (catalyst.getItems().length * 20);
            int index = adjustedTimer/20;
            return catalyst.getItems()[index].getItem();
        }
        else return item;
    }
}
