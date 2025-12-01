package com.pression.compressedbotanics.mixin.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.common.item.BotaniaItems;

@Mixin(PetalApothecaryRecipeCategory.class)
public class ApothecaryJEIMixin {
    @Unique
    private static Component plus = Component.literal("+");
    @Unique
    private static int width = 0;

    static {
        width = Minecraft.getInstance().font.width(plus.getString());
    }

   /* @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lvazkii/botania/api/recipe/PetalApothecaryRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"), remap = false)
    private void addReagent(IRecipeLayoutBuilder builder, PetalApothecaryRecipe recipe, IFocusGroup focusGroup, CallbackInfo ci){
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 29).addIngredients(recipe.getReagent())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.translatable("compressedbotanics.jei.catalyst")));
    }
    @Inject(method = "draw(Lvazkii/botania/api/recipe/PetalApothecaryRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V", at = @At("HEAD"), remap = false)
    private void addPlus(PetalApothecaryRecipe recipe, IRecipeSlotsView slotsView, GuiGraphics gui, double mouseX, double mouseY, CallbackInfo ci){
        RenderSystem.enableBlend();
        Font font = Minecraft.getInstance().font;
        gui.drawString(font, plus.getVisualOrderText(), 48-width , 38-(font.lineHeight /2), 0xFFFFFF);
        RenderSystem.disableBlend();
    }*/

    @ModifyArg(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lvazkii/botania/api/recipe/PetalApothecaryRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V"
            , at = @At(value = "INVOKE", target = "Lvazkii/botania/client/integration/jei/PetalApothecaryRecipeCategory;setRecipeLayout(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Ljava/util/List;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/ItemStack;[Lnet/minecraft/world/item/crafting/Ingredient;)V"),
            remap = false, index = 4)
    //Change the water bucket catalyst to also show the bowl of water
    private Ingredient[] deleteOGCatalyst(Ingredient[] reagents){
        return new Ingredient[]{
                Ingredient.of(Items.WATER_BUCKET, BotaniaItems.waterBowl),
                reagents[reagents.length-1]
        };
    }

}
