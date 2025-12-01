package com.pression.compressedbotanics.mixin.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pression.compressedbotanics.recipe.IRunicRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.client.integration.jei.RunicAltarRecipeCategory;

@Mixin(RunicAltarRecipeCategory.class)
public class RunicAltarJEIMixin {
    @Unique
    private static Component plus = Component.literal("+");
    @Unique
    private static int width = 0;
    @Unique
    private static IDrawable NO_CATALYST_ICON;
    static {
        width = Minecraft.getInstance().font.width(plus.getString());
    }
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void createIcon(IGuiHelper guiHelper, CallbackInfo ci){
        NO_CATALYST_ICON = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.BARRIER.getDefaultInstance());
    }

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lvazkii/botania/api/recipe/RunicAltarRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"), remap = false)
    private void addReagent(IRecipeLayoutBuilder builder, RunicAltarRecipe recipe, IFocusGroup focusGroup, CallbackInfo ci){
        if(!((IRunicRecipe)recipe).getCatalyst().isEmpty()) builder.addSlot(RecipeIngredientRole.INPUT, 48, 29).addIngredients(((IRunicRecipe)recipe).getCatalyst())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.translatable("compressedbotanics.jei.catalyst")));
    }

    @Inject(method = "draw(Lvazkii/botania/api/recipe/RunicAltarRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V", at = @At("HEAD"), remap = false)
    private void onDraw(RunicAltarRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY, CallbackInfo ci){
        RenderSystem.enableBlend();
        if(((IRunicRecipe)recipe).getCatalyst().isEmpty()){
            NO_CATALYST_ICON.draw(gui, 48, 29);
        }

        Font font = Minecraft.getInstance().font;
        gui.drawString(font, plus.getVisualOrderText(), 48 - width, 36 - (font.lineHeight / 2), 0xFFFFFF);

        RenderSystem.disableBlend();
    }

    @ModifyArg(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lvazkii/botania/api/recipe/RunicAltarRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V"
    , at = @At(value = "INVOKE", target = "Lvazkii/botania/client/integration/jei/PetalApothecaryRecipeCategory;setRecipeLayout(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Ljava/util/List;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/ItemStack;[Lnet/minecraft/world/item/crafting/Ingredient;)V"),
    remap = false, index = 4)
    //We already add the catalyst, wipe the hardcoded one
    private Ingredient[] deleteOGCatalyst(Ingredient[] reagents){
        return new Ingredient[]{};
    }

}
