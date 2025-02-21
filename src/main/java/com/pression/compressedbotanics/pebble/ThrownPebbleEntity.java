package com.pression.compressedbotanics.pebble;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.item.BotaniaItems;

public class ThrownPebbleEntity extends ThrowableItemProjectile {

    public ThrownPebbleEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownPebbleEntity(LivingEntity thrower) {
        super(PebbleRegistry.THROWN_PEBBLE.get(), thrower, thrower.level);
    }

    public ThrownPebbleEntity(Level world, double x, double y, double z) {
        super(PebbleRegistry.THROWN_PEBBLE.get(), x, y, z, world);
    }
    
    @Override
    protected void onHitEntity(@NotNull EntityHitResult hit){
        super.onHitEntity(hit);
        Entity entity = hit.getEntity();
        if(entity instanceof LivingEntity living){
            //Deal between nothing and half a heart of damage
            living.hurt(DamageSource.thrown(this, this.getOwner()), (float) Math.random());
        }
        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return BotaniaItems.pebble;
    }
}
