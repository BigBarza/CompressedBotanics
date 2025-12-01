package com.pression.compressedbotanics.recipe;

import net.minecraft.world.item.crafting.Ingredient;

//This interface lets us access the catalyst field from a RunigAltarRecipe
public interface IRunicRecipe {
    void setCatalyst(Ingredient ingredient);
    Ingredient getCatalyst();
}
