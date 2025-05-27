package com.pression.compressedbotanics.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.integration.jei.PureDaisyRecipeCategory;

@Mixin(PureDaisyRecipeCategory.class)
public class PureDaisyJEIMixin {
    @Inject(method = "draw(Lvazkii/botania/api/recipe/PureDaisyRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V", at = @At("HEAD"), remap = false)
    private void drawTimer(PureDaisyRecipe recipe, IRecipeSlotsView slotsView, PoseStack ms, double mouseX, double mouseY, CallbackInfo ci){
        RenderSystem.enableBlend();
        //Botania processes one block at a time, so on average, the timer is multiplied by 8
        int timer = recipe.getTime()*8;
        Component timerText = Component.literal((timer > 100) ? timer/20+"s" : timer+"t");
        Font font = Minecraft.getInstance().font;
        font.draw(ms, timerText, 18-((float) font.width(timerText.getString()) /2), 33, 0x888888);
        RenderSystem.disableBlend();
    }
}
