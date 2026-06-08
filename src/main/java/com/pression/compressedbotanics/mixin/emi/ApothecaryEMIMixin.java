package com.pression.compressedbotanics.mixin.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.client.integration.emi.PetalApothecaryEmiRecipe;
import vazkii.botania.common.item.BotaniaItems;


//This is a translation of ApothecaryJEIMixin, but applied to Botania's native EMI compat.
@Mixin(PetalApothecaryEmiRecipe.class)
public class ApothecaryEMIMixin {

    //This shows that a bowl of water can also fill the apothecary
    @ModifyArg(method = "addWidgets",
    at = @At(value = "INVOKE", target = "Lvazkii/botania/client/integration/emi/RunicAltarEmiRecipe;addRunicAltarWidgets(Ldev/emi/emi/api/widget/WidgetHolder;Ldev/emi/emi/api/recipe/EmiRecipe;Ljava/util/List;Ldev/emi/emi/api/stack/EmiIngredient;Ldev/emi/emi/api/stack/EmiStack;[Ldev/emi/emi/api/stack/EmiIngredient;)V"),
    remap = false, index = 5)
    private EmiIngredient[] replaceCatalysts(EmiIngredient[] original){
        return new EmiIngredient[]{
                original[0],
                EmiIngredient.of(Ingredient.of(Items.WATER_BUCKET, BotaniaItems.waterBowl))
        };
    }

}
