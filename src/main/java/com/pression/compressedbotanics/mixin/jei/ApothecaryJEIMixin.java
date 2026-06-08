package com.pression.compressedbotanics.mixin.jei;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.common.item.BotaniaItems;

@Mixin(PetalApothecaryRecipeCategory.class)
public class ApothecaryJEIMixin {

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
