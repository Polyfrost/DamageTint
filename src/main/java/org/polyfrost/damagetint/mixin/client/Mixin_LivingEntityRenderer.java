package org.polyfrost.damagetint.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
//? if >=1.21.4 {
import org.polyfrost.damagetint.client.utils.DamageTintRenderState;
//?}
import org.polyfrost.damagetint.client.utils.DamageVariant;
//? if <1.21.4 {
/*import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
*///?}
import org.polyfrost.damagetint.client.utils.OverlayCoords;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class Mixin_LivingEntityRenderer {

    //? if >=1.21.4 {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float f, CallbackInfo ci) {
        ((DamageTintRenderState) state).damageTint$extract(entity);
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlayCoords(LivingEntityRenderState state, float f, CallbackInfoReturnable<Integer> cir) {
        DamageTintRenderState data = (DamageTintRenderState) state;
        damageTint$overrideOverlayCoords(cir, data.damageTint$getHurtTime(), data.damageTint$getDeathTime(), data.damageTint$getVariant(), f);
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
