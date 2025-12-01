package com.pression.compressedbotanics.mixin.runic_altar;

import com.google.gson.JsonObject;
import com.pression.compressedbotanics.recipe.IRunicRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.recipe.HeadRecipe;

//This is nearly a clone of the other serializer mixin, to handle that player head crafting recipe.
@Mixin(HeadRecipe.Serializer.class)
public class HeadRecipeSerializerMixin {
    @Inject(method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lvazkii/botania/common/crafting/recipe/HeadRecipe;",
    at = @At("RETURN"), remap = false, cancellable = true)
    private void serializeCatalyst(ResourceLocation id, JsonObject json, CallbackInfoReturnable<HeadRecipe> cir){
        HeadRecipe recipe = cir.getReturnValue();
        Ingredient catalyst;
        if(json.has("use_catalyst") && !json.get("use_catalyst").getAsBoolean()){
            //If that field exists and is set to false, force an empty catalyst.
            catalyst = Ingredient.EMPTY;
        }
        //Otherwise, check for a catalyst. USE LIVINGROCK AS A DEFAULT.
        else {
            if(json.has("catalyst")){
                catalyst = Ingredient.fromJson(json.get("catalyst"));

            }
            else { //Default livingrock catalyst
                catalyst = Ingredient.of(BotaniaBlocks.livingrock.asItem());
            }
        }
        //Finally, set the catalyst item.
        ((IRunicRecipe) recipe).setCatalyst(catalyst);
        cir.setReturnValue(recipe);
    }

    @Inject(method = "fromNetwork(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lvazkii/botania/common/crafting/recipe/HeadRecipe;",
    at = @At("RETURN"), remap = false, cancellable = true)
    private void receiveCatalyst(ResourceLocation id, FriendlyByteBuf buf, CallbackInfoReturnable<HeadRecipe> cir){
        HeadRecipe recipe = cir.getReturnValue();
        Ingredient catalyst = Ingredient.fromNetwork(buf);
        ((IRunicRecipe) recipe).setCatalyst(catalyst);
        cir.setReturnValue(recipe);
    }

    @Inject(method = "toNetwork(Lnet/minecraft/network/FriendlyByteBuf;Lvazkii/botania/common/crafting/recipe/HeadRecipe;)V",
    at = @At("TAIL"), remap = false)
    private void sendCatalyst(FriendlyByteBuf buf, HeadRecipe recipe, CallbackInfo ci){
        Ingredient catalyst = ((IRunicRecipe) recipe).getCatalyst();
        catalyst.toNetwork(buf);
    }


}
