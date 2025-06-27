package com.pression.compressedbotanics.mixin.runic_altar;

import com.pression.compressedbotanics.CommonConfig;
import com.pression.compressedbotanics.recipe.IRunicRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.RunicAltarBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.Optional;

//We got a lot of hardcoded checks for livingrock here.
//The goal of this mixin is to replace all of them with checks for the recipe's catalyst item.
@Mixin(RunicAltarBlockEntity.class)
public class RunicAltarMixin {

    @Shadow(remap = false)
    private RunicAltarRecipe currentRecipe;

    @Redirect(method = "addItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"), remap = true)
    private boolean checkCatalystAddItem(ItemStack stack, Item item){
        if(item == BotaniaBlocks.livingrock.asItem()){
            if(updateCurrentRecipe()) return ((IRunicRecipe) currentRecipe).getCatalyst().test(stack);
            else return false;
        }
        //There's other checks, we only need to replace one.
        return stack.is(item);
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"), remap = false)
    private static boolean checkCatalystServerTick(ItemStack stack, Item item, Level level, BlockPos worldPosition, BlockState state, RunicAltarBlockEntity self){
        if(item == BotaniaBlocks.livingrock.asItem()){
            Optional<RunicAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, self.getItemHandler(), level);
            if(recipe.isPresent()){
                ((IRunicRecipe) recipe.get()).getCatalyst().test(stack);
            }
            else return false;
        }
        return stack.is(item);
    }

    @Redirect(method = "onUsedByWand", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"), remap = false)
    private boolean checkCatalystOnUsedByWand(ItemStack stack, Item item){
        if(item == BotaniaBlocks.livingrock.asItem()){
            if(updateCurrentRecipe()) return ((IRunicRecipe) currentRecipe).getCatalyst().test(stack);
            else return false;
        }
        return stack.is(item);
    }

    //This is a dirty hack to disable giving back runes from runic altar crafts.
    @Redirect(method = "onUsedByWand", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;"))
    private Abilities checkRuneRefundConfig(Player player){
        if(CommonConfig.NO_RUNE_REFUND.get()){
            //Yup. It's as jank as it looks. This was the best injection point, so just pass a dummy Abilities object.
            Abilities dummy = new Abilities();
            dummy.instabuild = true;
            return dummy;
        }
        else return player.getAbilities();
    }

    @ModifyVariable(method = "onUsedByWand", at = @At("STORE"), index = 6, remap = false)
    private ItemEntity createDefaultEntity(ItemEntity value) {
        if (value == null) {
            RunicAltarBlockEntity self = (RunicAltarBlockEntity) (Object) this;
            if (self.getLevel() != null) {
                Optional<RunicAltarRecipe> recipe = self.getLevel().getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, self.getItemHandler(), self.getLevel());
                if(recipe.isPresent() && ((IRunicRecipe)recipe.get()).getCatalyst() == Ingredient.EMPTY) {
                    return new ItemEntity(self.getLevel(), self.getBlockPos().getX(), self.getBlockPos().getY(), self.getBlockPos().getZ(), Items.BEDROCK.getDefaultInstance());
                }
            }
        }
        return value;
    }


    //We refer to the currentRecipe a lot here.
    //So we must make sure it is available.
    @Unique
    private boolean updateCurrentRecipe(){
        if(currentRecipe != null) return true;
        else{
            RunicAltarBlockEntity self = (RunicAltarBlockEntity)(Object) this;
            if(self.getLevel() != null){
                Optional<RunicAltarRecipe> recipe = self.getLevel().getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, self.getItemHandler(), self.getLevel());
                if(recipe.isPresent()){
                    currentRecipe = recipe.get();
                    return true;
                }
            }
        }

        return false;
    }

}
