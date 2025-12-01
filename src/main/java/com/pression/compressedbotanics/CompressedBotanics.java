package com.pression.compressedbotanics;

import com.mojang.logging.LogUtils;
import com.pression.compressedbotanics.pebble.PebbleRegistry;
import com.pression.compressedbotanics.pebble.ThrownPebbleEntity;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipeType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import vazkii.botania.common.item.BotaniaItems;

@Mod(CompressedBotanics.MODID)
public class CompressedBotanics {
    public static final String MODID = "compressedbotanics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CompressedBotanics() {
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FloralEntropyRecipeType.RECIPE_TYPES.register(modEventBus);
        FloralEntropyRecipeType.RECIPE_SERIALIZERS.register(modEventBus);
        PebbleRegistry.ENTITIES.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    //For some bloody reason, these don't work elsewhere. So they'll just stay here.
    public void clientSetup(FMLClientSetupEvent event){
        EntityRenderers.register(PebbleRegistry.THROWN_PEBBLE.get(), ThrownItemRenderer::new);
    }
    //Yes, this looks cursed. But hey, if it works...
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(BotaniaItems.pebble, new AbstractProjectileDispenseBehavior() {
                @Override
                protected @NotNull Projectile getProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack stack) {
                    ThrownPebbleEntity pebble = new ThrownPebbleEntity(PebbleRegistry.THROWN_PEBBLE.get(), level);
                    pebble.setPos(position.x(), position.y(), position.z());
                    return pebble;
                }
                @Override
                //Change this to set the accuracy, default is 6
                protected float getUncertainty(){
                    return 10f;
                }
                //Change this to set the speed, this is NOT the damage!
                protected float getPower(){
                    return (float) (Math.random()/2)+0.25f;
                }
            });
        });
    }

}