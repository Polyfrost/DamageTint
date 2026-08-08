package org.polyfrost.damagetint.mixin.client;

//? if > 1.8.9 {
import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//~ if < 26 'state.level.CameraRenderState' -> 'state.CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else
//import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
//? if >= 1.21.4 {
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Unique;
//?} else {
/*import net.minecraft.client.render.entity.EnderDragonRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.living.mob.monster.boss.EnderDragonEntity;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21.4 {
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
//?}

@Mixin(EnderDragonRenderer.class)
public class Mixin_EnderDragonRenderer {

    //? if > 1.8.9 {
    //? if >= 1.21.4 {
    @Unique
    private final Map<EnderDragonRenderState, Integer> damageTint$hurtTimeMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At("HEAD"))
    private void damageTint$recordHurtTime(EnderDragon entity, EnderDragonRenderState state, float partialTicks, CallbackInfo ci) {
        damageTint$hurtTimeMap.put(state, entity.hurtTime);
    }
    //?}

    //? if >= 1.21.10 {
    @ModifyExpressionValue(
            //~ if < 26 'state/level/CameraRenderState' -> 'state/CameraRenderState'
            method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        return damageTint$fadeOverlay(original, state.hasRedOverlay, damageTint$hurtTimeMap.getOrDefault(state, 0));
    }
    //?} elif >= 1.21.4 {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$fadeOverlay(original, state.hasRedOverlay, damageTint$hurtTimeMap.getOrDefault(state, 0));
    }
    *///?} else {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$fadeOverlay(original, entity.hurtTime > 0, entity.hurtTime);
    }
    *///?}

    @Unique
    private static int damageTint$fadeOverlay(int original, boolean hasRedOverlay, int hurtTime) {
        if (!DamageTintConfig.enabled || !DamageTintConfig.fade || !hasRedOverlay) {
            return original;
        }

        int row = Math.round((1.0f - (float) hurtTime / DamageTintConfig.fadeDuration) * 7.0f);
        row = Math.clamp(row, 0, 7);
        return OverlayTexture.pack(original & 0xFFFF, row);
    }
    //?} else {
    /*@Inject(
            method = "renderModel(Lnet/minecraft/entity/living/mob/monster/boss/EnderDragonEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/platform/GlStateManager;color4f(FFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void damageTint$modifyTint(EnderDragonEntity entity, float limbAngle, float limbDistance, float tickDelta, float yaw, float pitch, float scale, CallbackInfo ci) {
        if (!DamageTintConfig.enabled) {
            return;
        }

        int argb = DamageTintConfig.colorV2.getArgb();
        float alpha = (argb >>> 24) / 255.0f;
        float r = (argb >> 16 & 0xFF) / 255.0f;
        float g = (argb >> 8 & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        if (DamageTintConfig.fade) {
            alpha *= Math.clamp(entity.damagedTimer / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
        }

        GlStateManager.color4f(r, g, b, alpha);
    }
    *///?}
}
