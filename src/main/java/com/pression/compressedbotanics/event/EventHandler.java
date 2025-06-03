package com.pression.compressedbotanics.event;

import com.pression.compressedbotanics.CommonConfig;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.block.block_entity.CocoonBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    //This event handler blocks certain items from being used on the cocoon of caprice when certain configs are set.
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        if(event.getLevel().getBlockEntity(event.getPos()) instanceof CocoonBlockEntity){
            if(CommonConfig.COCOON_NO_EMERALD.get() && event.getItemStack().is(Items.EMERALD)) event.setCanceled(true);
            if(CommonConfig.COCOON_NO_CHORUS.get() && event.getItemStack().is(Items.CHORUS_FRUIT)) event.setCanceled(true);
            if(CommonConfig.COCOON_NO_GAIA.get() && event.getItemStack().is(BotaniaItems.lifeEssence)) event.setCanceled(true);
        }
    }
}
