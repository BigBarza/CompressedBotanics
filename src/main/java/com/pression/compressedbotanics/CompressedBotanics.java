package com.pression.compressedbotanics;

import com.mojang.logging.LogUtils;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CompressedBotanics.MODID)
public class CompressedBotanics {
    public static final String MODID = "compressedbotanics";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CompressedBotanics() {
        LOGGER.info("Hexagons are the bestagons!");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FloralEntropyRecipeType.RECIPE_TYPES.register(modEventBus);
        FloralEntropyRecipeType.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}