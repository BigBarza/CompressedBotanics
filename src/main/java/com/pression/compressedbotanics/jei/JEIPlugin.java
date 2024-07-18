package com.pression.compressedbotanics.jei;

import com.pression.compressedbotanics.CompressedBotanics;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipe;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

//This is just some registration stuff. JEI sees this and does its thing.
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid(){
        return new ResourceLocation(CompressedBotanics.MODID, "jei_plugin");
    }
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FloralEntropyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FloralEntropyRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FloralEntropyRecipeType.FLORAL_ENTROPY_RECIPE_TYPE.get());
        registration.addRecipes(FloralEntropyRecipeCategory.TYPE, recipes);
    }
}
