package org.polyfrost.damagetint.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//~ if < 26 'state.level.CameraRenderState' -> 'state.CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} else
//import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.polyfrost.damagetint.client.utils.ArmorOverlayContext;
import org.polyfrost.damagetint.client.utils.DamageVariant;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.polyfrost.damagetint.client.utils.OverlayCoords;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.21.4 {
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
//?}

@Mixin(LivingEntityRenderer.class)
public abstract class Mixin_LivingEntityRenderer {

    //? if >= 1.21.4 {
    @Shadow
    protected abstract float getWhiteOverlayProgress(LivingEntityRenderState state);
    //?} else {
    /*@Shadow
    protected abstract float getWhiteOverlayProgress(LivingEntity entity, float partialTicks);
    *///?}

    //? if >= 1.21.10 {
    @WrapMethod(
            //~ if < 26 'state/level/CameraRenderState' -> 'state/CameraRenderState'
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
    )
    private void damageTint$withArmorOverlay(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, Operation<Void> original) {
        ArmorOverlayContext.begin();
        try {
            if (DamageTintConfig.enabled && DamageTintConfig.tintArmor && state.hasRedOverlay) {
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, getWhiteOverlayProgress(state));
                ArmorOverlayContext.capture(overlayCoords, true);
            }
            original.call(state, poseStack, submitNodeCollector, camera);
        } finally {
            ArmorOverlayContext.end();
        }
    }
    //?} elif >= 1.21.4 {
    /*@WrapMethod(
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void damageTint$withArmorOverlay(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Operation<Void> original) {
        ArmorOverlayContext.begin();
        try {
            if (DamageTintConfig.enabled && DamageTintConfig.tintArmor && state.hasRedOverlay) {
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, getWhiteOverlayProgress(state));
                ArmorOverlayContext.capture(overlayCoords, true);
            }
            original.call(state, poseStack, bufferSource, packedLight);
        } finally {
            ArmorOverlayContext.end();
        }
    }
    *///?} else {
    /*@WrapMethod(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void damageTint$withArmorOverlay(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Operation<Void> original) {
        ArmorOverlayContext.begin();
        try {
            boolean hasRedOverlay = entity.hurtTime > 0 || entity.deathTime > 0;
            if (DamageTintConfig.enabled && DamageTintConfig.tintArmor && hasRedOverlay) {
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(entity, getWhiteOverlayProgress(entity, partialTicks));
                ArmorOverlayContext.capture(overlayCoords, true);
            }
            original.call(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        } finally {
            ArmorOverlayContext.end();
        }
    }
    *///?}

    //? if >=1.21.4 {
    @Unique
    private static final Map<Object, Integer> damageTint$hurtTimeMap = Collections.synchronizedMap(new WeakHashMap<>());
    @Unique
    private static final Map<Object, Integer> damageTint$deathTimeMap = Collections.synchronizedMap(new WeakHashMap<>());
    @Unique
    private static final Map<Object, DamageVariant> damageTint$variantMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float f, CallbackInfo ci) {
        damageTint$hurtTimeMap.put(state, entity.hurtTime);
        damageTint$deathTimeMap.put(state, entity.deathTime);
        damageTint$variantMap.put(state, DamageVariantTracker.get(entity));
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlayCoords(LivingEntityRenderState state, float f, CallbackInfoReturnable<Integer> cir) {
        Integer hurtTimeObj = damageTint$hurtTimeMap.get(state);
        Integer deathTimeObj = damageTint$deathTimeMap.get(state);
        int hurtTime = hurtTimeObj != null ? hurtTimeObj : 0;
        int deathTime = deathTimeObj != null ? deathTimeObj : 0;
        DamageVariant variant = damageTint$variantMap.getOrDefault(state, DamageVariant.OTHER);

        damageTint$overrideOverlayCoords(cir, hurtTime, deathTime, variant, f);
    }
    //?} else {
    /*@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlayCoords(LivingEntity entity, float f, CallbackInfoReturnable<Integer> cir) {
        damageTint$overrideOverlayCoords(cir, entity.hurtTime, entity.deathTime, DamageVariantTracker.get(entity), f);
    }
    *///?}

    @Unique
    private static void damageTint$overrideOverlayCoords(CallbackInfoReturnable<Integer> cir, int hurtTime, int deathTime, DamageVariant variant, float whiteOverlayProgress) {
        boolean hasRedOverlay = hurtTime > 0 || deathTime > 0;
        int coords = OverlayCoords.of(hasRedOverlay, hurtTime, deathTime, variant, OverlayTexture.u(whiteOverlayProgress));
        if (coords != OverlayCoords.NO_OVERRIDE) {
            cir.setReturnValue(coords);
        }
    }
}
