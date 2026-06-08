package com.pression.compressedbotanics.mixin.emi;

import com.pression.compressedbotanics.recipe.IRunicRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.client.integration.emi.RunicAltarEmiRecipe;

@Mixin(RunicAltarEmiRecipe.class)
public class RunicAltarEMIMixin {
    @Unique private Ingredient catalyst = Ingredient.EMPTY;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void captureTimer(RunicAltarRecipe recipe, CallbackInfo ci){
        catalyst = ((IRunicRecipe)recipe).getCatalyst();
    }


    @ModifyArg(method = "addWidgets",
    at = @At(value = "INVOKE", target = "Lvazkii/botania/client/integration/emi/RunicAltarEmiRecipe;addRunicAltarWidgets(Ldev/emi/emi/api/widget/WidgetHolder;Ldev/emi/emi/api/recipe/EmiRecipe;Ljava/util/List;Ldev/emi/emi/api/stack/EmiIngredient;Ldev/emi/emi/api/stack/EmiStack;[Ldev/emi/emi/api/stack/EmiIngredient;)V"),
    remap = false, index = 5)
    private EmiIngredient[] replaceCatalysts(EmiIngredient[] original){
        return catalyst.isEmpty() ? new EmiIngredient[]{} : new EmiIngredient[]{ EmiIngredient.of(catalyst) };
    }

}
