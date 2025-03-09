package com.pression.compressedbotanics.pebble;

import com.pression.compressedbotanics.CompressedBotanics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PebbleRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CompressedBotanics.MODID);

    public static final RegistryObject<EntityType<ThrownPebbleEntity>> THROWN_PEBBLE = ENTITIES.register("thrown_pebble", () -> EntityType.Builder.<ThrownPebbleEntity>of(ThrownPebbleEntity::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(new ResourceLocation(CompressedBotanics.MODID, "thrown_pebble").toString()));

    public static void register(IEventBus bus){
        ENTITIES.register(bus);
    }

}
