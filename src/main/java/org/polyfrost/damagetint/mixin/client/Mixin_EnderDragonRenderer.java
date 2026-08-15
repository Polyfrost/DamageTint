package org.polyfrost.damagetint.mixin.client;

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
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.polyfrost.damagetint.client.utils.DamageVariant;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.polyfrost.damagetint.client.utils.OverlayCoords;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21.4 {
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
//?}

@Mixin(EnderDragonRenderer.class)
public class Mixin_EnderDragonRenderer {

    //? if >= 1.21.4 {
    @Unique
    private final Map<EnderDragonRenderState, Integer> damageTint$hurtTimeMap = Collections.synchronizedMap(new WeakHashMap<>());
    @Unique
    private final Map<EnderDragonRenderState, DamageVariant> damageTint$variantMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At("HEAD"))
    private void damageTint$recordHurtTime(EnderDragon entity, EnderDragonRenderState state, float partialTicks, CallbackInfo ci) {
        damageTint$hurtTimeMap.put(state, entity.hurtTime);
        damageTint$variantMap.put(state, DamageVariantTracker.get(entity));
    }
    //?}

    //? if >= 1.21.10 {
    @ModifyExpressionValue(
            //~ if < 26 'state/level/CameraRenderState' -> 'state/CameraRenderState'
            method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        return damageTint$overlayCoords(original, state);
    }
    //?} elif >= 1.21.4 {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$overlayCoords(original, state);
    }
    *///?} else {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$overlayCoords(original, entity.hurtTime > 0, entity.hurtTime, DamageVariantTracker.get(entity));
    }
    *///?}

    //? if >= 1.21.4 {
    @Unique
    private int damageTint$overlayCoords(int original, EnderDragonRenderState state) {
        return damageTint$overlayCoords(original, state.hasRedOverlay, damageTint$hurtTimeMap.getOrDefault(state, 0),
                damageTint$variantMap.getOrDefault(state, DamageVariant.OTHER));
    }
    //?}

    @Unique
    private static int damageTint$overlayCoords(int original, boolean hasRedOverlay, int hurtTime, DamageVariant variant) {
        int coords = OverlayCoords.of(hasRedOverlay, hurtTime, 0, variant, original & 0xFFFF);
        return coords == OverlayCoords.NO_OVERRIDE ? original : coords;
    }
}
