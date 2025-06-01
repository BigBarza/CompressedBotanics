package com.pression.compressedbotanics.mixin.runic_altar;

import com.pression.compressedbotanics.recipe.IRunicRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.common.crafting.RunicAltarRecipe;

//This mixin adds an Ingredient field to runic altar recipes, to represent the catalyst item.
@Mixin(RunicAltarRecipe.class)
public class RunicAltarRecipeMixin implements IRunicRecipe {
    @Unique
    private Ingredient catalyst;

    @Unique
    public void setCatalyst(Ingredient ingredient){
        this.catalyst = ingredient;
    }

    @Unique
    public Ingredient getCatalyst(){
        return catalyst;
    }
}
