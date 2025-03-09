package com.pression.compressedbotanics.recipe;

import com.pression.compressedbotanics.CompressedBotanics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FloralEntropyRecipeType { //I have no goddamn clue how long this took to get working, documentation is a mess.
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CompressedBotanics.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, CompressedBotanics.MODID);

    public static final RegistryObject<RecipeType<FloralEntropyRecipe>> FLORAL_ENTROPY_RECIPE_TYPE = RECIPE_TYPES.register("floral_entropy",
            () -> new RecipeType<FloralEntropyRecipe>() {
                @Override
                public String toString(){
                    return new ResourceLocation(CompressedBotanics.MODID, "floral_entropy").toString();
                }
            }
    );

    public static final RegistryObject<RecipeSerializer<FloralEntropyRecipe>> FLORAL_ENTROPY_SERIALIZER = RECIPE_SERIALIZERS.register("floral_entropy", FloralEntropyRecipe.Serializer::new);

}
