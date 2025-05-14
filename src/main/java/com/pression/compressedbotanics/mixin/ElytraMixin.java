package com.pression.compressedbotanics.mixin;

import com.pression.compressedbotanics.CommonConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

@Mixin(ElytraItem.class)
public class ElytraMixin {

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if(!CommonConfig.ELYTRA_MANA_REPAIR.get()) return;
        if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, CommonConfig.ELYTRA_MANA_REPAIR_COST.get(), true)) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if(!CommonConfig.ELYTRA_MANA_REPAIR.get()) return amount;
        return ToolCommons.damageItemIfPossible(stack, amount, entity, CommonConfig.ELYTRA_MANA_DAMAGE_COST.get());
    }
}
