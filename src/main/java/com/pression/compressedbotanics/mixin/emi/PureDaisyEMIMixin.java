package com.pression.compressedbotanics.mixin.emi;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.client.integration.emi.PureDaisyEmiRecipe;

@Mixin(PureDaisyEmiRecipe.class)
public class PureDaisyEMIMixin {

    @Unique
    private int time = -1;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void captureTimer(PureDaisyRecipe recipe, CallbackInfo ci){
        time = recipe.getTime()*8;
    }

    @Inject(method = "addWidgets", at = @At("TAIL"), remap = false)
    private void addTimer(WidgetHolder widgets, CallbackInfo ci){
        Component timerText = Component.literal((time > 100) ? time/20+"s" : time+"t");
        Font font = Minecraft.getInstance().font;
        widgets.addText(timerText, 18-(font.width(timerText.getString()) /2), 33, 0x888888, false);
    }
}
