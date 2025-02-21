package com.pression.compressedbotanics.pebble;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.item.BotaniaItems;

@Mod.EventBusSubscriber
public class PebbleYeetingHandler {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        //If we would be interacting with a block, don't throw the pebble.
        InteractionResult result = event.getLevel().getBlockState(event.getPos())
                .use(event.getLevel(), event.getEntity(), event.getHand(), event.getHitVec());
        if(result.consumesAction()){
            event.setCancellationResult(result);
            event.setCanceled(true);
            return;
        }
        //I don't think this does anything. It was meant to allow collecting pebbles with pebbles in the hand.
        if(!event.getEntity().isCrouching()) yeetPebble(event);
    }
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event){
        yeetPebble(event);

    }
    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event){
        yeetPebble(event);
    }

    private static void yeetPebble(PlayerInteractEvent event){
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if(stack.is(BotaniaItems.pebble)){
            //Really should register a separate sound event for this. It's only really going to affect stuff like subtitles.
            player.getLevel().playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
            if(!player.getLevel().isClientSide()){
                ThrownPebbleEntity pebble = new ThrownPebbleEntity(player);
                pebble.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, (float) (Math.random()/2)+0.25f, 10f);
                player.getLevel().addFreshEntity(pebble);
            }
            if(!player.getAbilities().instabuild){
                stack.shrink(1);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

}
