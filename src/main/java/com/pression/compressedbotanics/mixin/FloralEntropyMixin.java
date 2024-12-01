package com.pression.compressedbotanics.mixin;

import com.pression.compressedbotanics.CompressedBotanics;
import com.pression.compressedbotanics.recipe.ChanceOutput;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipe;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlocks;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(GeneratingFlowerBlockEntity.class)
public class FloralEntropyMixin {

    @Unique private int prevMana = 0; //Used to tally up mana
    @Unique private int manaTally = 0; //Used for the min generated mana condition.
    @Unique private boolean decayFlag = false; //This indicates when the flower CAN decay, assuming the mana condition passes.
    @Unique GeneratingFlowerBlockEntity flower = (GeneratingFlowerBlockEntity)(Object)this; //I don't know if it SHOULD work up here, but it does, so i'm not complaining.
    @Shadow(remap = false) private int mana; //This just accesses the flower's internal mana storage. DO NOT MODIFY HERE, call flower.addMana(int) instead.

    //currently unused
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(BlockEntityType type, BlockPos pos, BlockState state, CallbackInfo ci){

    }


    //These two are used together to tally up the total generated mana. Gotta keep those flowers empty!
    @Inject(method = "emptyManaIntoCollector", at = @At("HEAD"), remap = false)
    private void tallyManaStart(CallbackInfo ci){
        manaTally += (mana-prevMana);
    }
    @Inject(method = "emptyManaIntoCollector", at = @At("TAIL"), remap = false)
    private void tallyManaEnd(CallbackInfo ci){
        prevMana = mana;

    }

    //Turns out tallied mana does not get saved when restarting or rejoining in singleplayer. This fixes that.
    @Inject(method = "readFromPacketNBT", at = @At("TAIL"), remap = false)
    private void onReadNBT(CompoundTag cmp, CallbackInfo ci){
        if(cmp.contains("manaTally")) manaTally = cmp.getInt("manaTally");
        if(cmp.contains("decayFlag")) decayFlag = cmp.getBoolean("decayFlag");
        if(prevMana != mana) prevMana = mana; //Prevents gaining more tallied mana by resetting
    }
    //Also i'm aware it's not supposed to be these two methods, but they work, so i guess?
    @Inject(method = "writeToPacketNBT", at = @At("TAIL"), remap = false)
    private void onWriteNBT(CompoundTag cmp, CallbackInfo ci){
        cmp.putInt("manaTally", manaTally);
        cmp.putBoolean("decayFlag", decayFlag);
    }


    @Inject(method = "tickFlower", at = @At("TAIL"), remap = false)
    private void onTickFlower(CallbackInfo ci){
        //Additional failsafe for a null level.
        if(flower.getLevel() == null || flower.getLevel().isClientSide()) return; //Weird stuff happens if this is not checked.
        if(Math.random() < 0.05F){ //Note: if adjusting this, adjust also the chance multiplier in calcDecayChance
            FloralEntropyRecipe recipe = getResult(BlockEntityType.getKey(flower.getType()), (ServerLevel) flower.getLevel()); //NOTE: This does NOT care for the variant of flower, be it floating, or chibi (Petit).
            if(!decayFlag && recipe != null){
                    if(Math.random() <= calcDecayChance(flower.ticksExisted, recipe.getMaxDecayTicks(), recipe.getMinDecayTicks())) decayFlag = true;
            }
            if(recipe != null && decayFlag && manaTally >= recipe.getMinTalliedMana()) { //Recheck the recipe, in case it has reloaded. Even if i implement proper recipe reloading, decayflag probably shouldn't be reset.
                double decayChance = calcDecayChance(flower.ticksExisted, recipe.getMaxDecayTicks(), recipe.getMinDecayTicks());
                if(Math.random() <= decayChance) entropyTime(flower, recipe); //Decay chance rolled, it's e n t r o p y time.
            }
        }
    }

    private void entropyTime(GeneratingFlowerBlockEntity flower, FloralEntropyRecipe recipe){
        Level level = flower.getLevel();
        if(level == null){
            CompressedBotanics.LOGGER.error("Flower attempted to decay on a null level.");
        }
        BlockPos pos = flower.getBlockPos();
        flower.getLevel().destroyBlock(pos, false);
        BlockState decayedBlock = ForgeRegistries.BLOCKS.getValue(recipe.getBlock()).defaultBlockState();
        level.setBlockAndUpdate(pos, decayedBlock);
        if(!recipe.getResult().isEmpty()) {
            for (ChanceOutput output : recipe.getResult()) {
                if(output.isSpecial() && !isOvergrow(flower)) continue; //Special outputs can only be obtained by having the flower decay on enchanted soil.
                ItemStack item = output.rollItem();
                if (item != ItemStack.EMPTY) { //An empty itemstack is returned if an AoN roll fails or no rolls pass on a regular one.
                    ItemEntity droppedItem = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
                    double d0 = level.random.nextDouble() * 0.2 - 0.1;
                    double d1 = level.random.nextDouble() * 0.2;
                    double d2 = level.random.nextDouble() * 0.2 - 0.1;
                    droppedItem.setDeltaMovement(new Vec3(d0, d1, d2)); //Fling them around a bit.
                    level.addFreshEntity(droppedItem);
                }
            }
        }
    }


    @Nullable
    private FloralEntropyRecipe getResult(ResourceLocation flower, ServerLevel level){
        List<FloralEntropyRecipe> recipes = level.getRecipeManager().getAllRecipesFor(FloralEntropyRecipeType.FLORAL_ENTROPY_RECIPE_TYPE.get());
        for (FloralEntropyRecipe recipe : recipes){ //There's probably a better way to fo this as well.
            if(recipe.getFlower().equals(flower)) return recipe;
        }
        return null;
    }
    @Unique
    private boolean isOvergrow(GeneratingFlowerBlockEntity flower){ //This function is in SpecialFlowerBlockEntity but for SOME reason, it's inaccessible. So i'm making my own.
        if(flower.isFloating()) return false;
        return flower.getLevel().getBlockState(flower.getBlockPos().below()).is(BotaniaBlocks.enchantedSoil);
    }

    private double calcDecayChance(int timeOriginal, int max, int min){
        if(max <= 0) return 1; // because of fucking course the first one i try, i set a max time of 0.
        if(timeOriginal < min) return 0; //If we haven't passed the minimum time, the check should always fail.
        int time = timeOriginal; //Idk if it's a pointer, and i don't wanna find out.
        time -= min; //subtract min from both time and max since we want the curve to be from min to max
        max -=min;
        double chance = (time/max)/(max/2.5); //This would be the chance for if we rolled every tick.
        return chance*20; //But we're rolling a 1/20 beforehand so the chance needs to be adjusted.
    }

}


