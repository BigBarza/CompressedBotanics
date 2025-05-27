package com.pression.compressedbotanics.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.PetalApothecaryRecipe;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;

@Mixin(PetalApothecaryRecipeCategory.class)
public class ApothecaryJEIMixin {
    @Unique
    private static Component plus = Component.literal("+");
    @Unique
    private static int width = 0;

    static {
        width = Minecraft.getInstance().font.width(plus.getString());
    }

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lvazkii/botania/api/recipe/PetalApothecaryRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"), remap = false)
    private void addReagent(IRecipeLayoutBuilder builder, PetalApothecaryRecipe recipe, IFocusGroup focusGroup, CallbackInfo ci){
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 29).addIngredients(recipe.getReagent())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.translatable("compressedbotanics.jei.apothecary_reagent")));
    }
    @Inject(method = "draw(Lvazkii/botania/api/recipe/PetalApothecaryRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V", at = @At("HEAD"), remap = false)
    private void addPlus(PetalApothecaryRecipe recipe, IRecipeSlotsView slotsView, PoseStack ms, double mouseX, double mouseY, CallbackInfo ci){
        RenderSystem.enableBlend();
        Font font = Minecraft.getInstance().font;
        font.drawShadow(ms, plus, 48-width , 38-((float) font.lineHeight /2), 0xFFFFFF);
        RenderSystem.disableBlend();
    }


}
