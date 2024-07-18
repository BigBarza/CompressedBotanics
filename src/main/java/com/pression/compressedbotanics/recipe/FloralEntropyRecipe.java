package com.pression.compressedbotanics.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

//This holds all the info serialized from the data.
public class FloralEntropyRecipe implements Recipe<Inventory> {
    private final ResourceLocation id;
    private final ResourceLocation flower;
    private final ResourceLocation block;
    private final List<ChanceOutput> result;
    private final int minDecayTicks;
    private final int maxDecayTicks;
    private final int minMana;

    public FloralEntropyRecipe(ResourceLocation id, ResourceLocation flower, ResourceLocation block, List<ChanceOutput> result, int minTime, int maxTime, int minMana){
        this.id = id;
        this.flower = flower;
        this.block = block;
        this.result = result;
        this.minDecayTicks = minTime;
        this.maxDecayTicks = maxTime;
        this.minMana = minMana;
    }

    //mm yes, compressed functions.
    @Override public ResourceLocation getId(){
        return id;
    }
    public ResourceLocation getFlower(){
        return flower;
    }
    public ResourceLocation getBlock(){
        return block;
    }
    public int getMinDecayTicks(){
        return minDecayTicks;
    }
    public int getMaxDecayTicks(){
        return maxDecayTicks;
    }
    public int getMinTalliedMana(){
        return minMana;
    }
    public List<ChanceOutput> getResult() {
        return result;
    }
    @Override public ItemStack getResultItem(){
        return ItemStack.EMPTY;
    }
    @Override public RecipeSerializer<?> getSerializer(){return FloralEntropyRecipeType.FLORAL_ENTROPY_SERIALIZER.get();}
    @Override public RecipeType<?> getType(){
        return FloralEntropyRecipeType.FLORAL_ENTROPY_RECIPE_TYPE.get();
    }
    @Override public boolean matches(Inventory inv, Level world){
        return false;
    }
    @Override public ItemStack assemble(Inventory inv){
        return ItemStack.EMPTY;
    }
    @Override public boolean canCraftInDimensions(int w, int h){
        return false;
    }

    public ItemStack getFlowerAsItemStack(){
        return new ItemStack(ForgeRegistries.ITEMS.getValue(getFlower()), 1);
    }
    public ItemStack getBlockAsItemStack(){
        return new ItemStack(ForgeRegistries.ITEMS.getValue(getBlock()), 1);
    }

    public static class Serializer implements RecipeSerializer<FloralEntropyRecipe>{
        @Override
        public FloralEntropyRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation flower = new ResourceLocation(GsonHelper.getAsString(json, "flower"));
            ResourceLocation block = ForgeRegistries.BLOCKS.getKey(Blocks.AIR);
            if(json.has("block")) block = new ResourceLocation(GsonHelper.getAsString(json, "block"));
            List<ChanceOutput> decayResult = new ArrayList<>();
            if(json.has("result")) {
                JsonArray output = GsonHelper.getAsJsonArray(json, "result");
                for (JsonElement element : output) {
                    JsonObject obj = element.getAsJsonObject();
                    ItemStack item = ShapedRecipe.itemStackFromJson(obj);
                    float chance = 1;
                    if (obj.has("chance")) chance = GsonHelper.getAsFloat(obj, "chance");
                    boolean flag = false;
                    if (obj.has("allOrNothing")) flag = GsonHelper.getAsBoolean(obj, "allOrNothing");
                    decayResult.add(new ChanceOutput(item, chance, flag));
                }
            }
            int minTime = 0;
            if(json.has("minDecayTicks")) minTime = GsonHelper.getAsInt(json, "minDecayTicks");
            int maxTime = GsonHelper.getAsInt(json, "maxDecayTicks");
            int minMana = 0;
            if(json.has("minTalliedMana")) minMana = GsonHelper.getAsInt(json, "minTalliedMana");
            return new FloralEntropyRecipe(id, flower, block, decayResult, minTime, maxTime, minMana);
        }

        @Override
        public @Nullable FloralEntropyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ResourceLocation flower = buf.readResourceLocation();
            ResourceLocation block = buf.readResourceLocation();
            int outSize = buf.readInt();
            List<ChanceOutput> result = new ArrayList<>();
            for(int i=0; i<outSize; i++){
                ItemStack item = buf.readItem();
                float chance = buf.readFloat();
                boolean flag = buf.readBoolean();
                result.add(new ChanceOutput(item, chance, flag));
            }
            int minTime = buf.readInt();
            int maxTime = buf.readInt();
            int minMana = buf.readInt();
            return new FloralEntropyRecipe(id, flower, block, result, minTime, maxTime,minMana);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FloralEntropyRecipe recipe) {
            buf.writeResourceLocation(recipe.getFlower());
            buf.writeResourceLocation(recipe.getBlock());
            buf.writeInt(recipe.getResult().size());
            for(ChanceOutput out : recipe.getResult()){
                buf.writeItem(out.getItem());
                buf.writeFloat(out.getChance());
                buf.writeBoolean(out.isAllOrNothingFlag());
            }
            buf.writeInt(recipe.getMinDecayTicks());
            buf.writeInt(recipe.getMaxDecayTicks());
            buf.writeInt(recipe.getMinTalliedMana());
        }
    }

}

