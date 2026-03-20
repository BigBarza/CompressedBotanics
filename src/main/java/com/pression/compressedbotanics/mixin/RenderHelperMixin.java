package com.pression.compressedbotanics.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.ResourcesLib;

//This mixin is used to change the rendered thickness of red string, for better visibility.
@Mixin(RenderHelper.class)
public abstract class RenderHelperMixin {
    @Shadow(remap = false)
    private static RenderType makeLayer(String name, VertexFormat format, VertexFormat.Mode mode, int bufSize, RenderType.CompositeState glState) {
        return null;
    }

    @Shadow(remap = false)
    private static RenderType.CompositeState lineState(double width, boolean direct, boolean noDepth) {
        return null;
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lvazkii/botania/client/core/helper/RenderHelper;makeLayer(Ljava/lang/String;Lcom/mojang/blaze3d/vertex/VertexFormat;Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;ILnet/minecraft/client/renderer/RenderType$CompositeState;)Lnet/minecraft/client/renderer/RenderType;"), remap = false)
    private static RenderType changeRedStringThickness(String name, VertexFormat format, VertexFormat.Mode mode, int bufSize, RenderType.CompositeState glState){
        if(name.equals("botania:red_string")){
            return makeLayer(ResourcesLib.PREFIX_MOD + "red_string", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 128, lineState(4, false, false));
        }
        return makeLayer(name, format, mode, bufSize, glState);
    }
}
