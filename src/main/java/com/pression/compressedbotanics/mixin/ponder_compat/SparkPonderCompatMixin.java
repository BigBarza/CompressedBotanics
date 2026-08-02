package com.pression.compressedbotanics.mixin.ponder_compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedbotanics.mixin_interface.IPonderScene;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.foundation.PonderScene;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.client.render.entity.BaseSparkRenderer;
import vazkii.botania.common.entity.SparkBaseEntity;

@Mixin(BaseSparkRenderer.class)
public class SparkPonderCompatMixin {

    private static Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
    private static Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);

    @Redirect(method = "render(Lvazkii/botania/common/entity/SparkBaseEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;cameraOrientation()Lorg/joml/Quaternionf;"))
    private Quaternionf changeCamera(EntityRenderDispatcher instance, SparkBaseEntity spark, float p_114600_, float p_114601_, PoseStack p_114602_, MultiBufferSource p_114603_, int p_114604_){
        if(spark.level() instanceof PonderLevel ponder){
            PonderScene.SceneCamera camera = ((IPonderScene) ponder.scene).getCamera();
            // Right. I am not going to pretend to fully understand this as it would mean delving into
            // more complex modelling and rendering stuff. It's a fancier angle with a side of...imaginary numbers?
            // Some called this method a bit of a dirty hack. I don't care. It does what i need it to do.
            Quaternionf q = rotationDegrees(YP, -camera.getYRot());
            q.mul(rotationDegrees(XP, camera.getXRot()));
            //But yeah. If we're in a PonderWorld, use this camera angle instead.
            return q;
        }
        //Otherwise, use the normal one.
        return instance.cameraOrientation();
    }

    //This replicates the math i need from 1.19.2
    @Unique
    private static Quaternionf rotationDegrees(Vector3f vec, float angle){
        angle = angle * (float) Math.PI/180f;
        float f = (float) Math.sin(angle/2);
        double qx = vec.x() * f;
        double qy = vec.y() * f;
        double qz = vec.z() * f;
        double qr = Math.cos(angle/2);
        return new Quaternionf(qx, qy, qz, qr);
    }
}
