package com.pression.compressedbotanics.event;

import com.pression.compressedbotanics.CompressedBotanics;
import com.pression.compressedbotanics.mixin.FloralEntropyMixin;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;

//This event handler serves to punish players who try to cheese the mechanics by breaking flowers before they can decay.
@Mod.EventBusSubscriber(modid = CompressedBotanics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlowerBreakHandler {
    @SubscribeEvent
    public static void flowerBreakHandler(BlockEvent.BreakEvent event){
        BlockEntity be = event.getLevel().getBlockEntity(event.getPos());
        if(be instanceof GeneratingFlowerBlockEntity flower){
            CompoundTag tag = flower.getPersistentData();
            //If the flower doesn't have the tags, abort the process
            if(!tag.contains("manaTally") || !tag.contains("decayFlag")){
                CompressedBotanics.LOGGER.error("Generating flower broken at "+event.getPos().toString()+" didn't have the nbt tags!");
                return;
            }
            FloralEntropyRecipe recipe = FloralEntropyMixin.getResult(BlockEntityType.getKey(flower.getType()), (ServerLevel) flower.getLevel());
            if(recipe == null) return; //If this flower can't decay at all, abort.
            //The chance to fail to pickup the flower. A flower that generated 30% of the requirement will have a 30% chance to drop nothing.
            double failChance = ((double) tag.getInt("manaTally"))/((double)recipe.getMinTalliedMana());
            if(Math.random() < failChance){
                //Prevent the normal break from occurring, which would drop the flower.
                event.setCanceled(true);
                //This destroys the block without dropping anything.
                event.getLevel().destroyBlock(event.getPos(), false);
                //And a dead bush because funni
                Block.popResource(flower.getLevel(), event.getPos(), new ItemStack(Items.DEAD_BUSH));
            }
        }
    }
}
