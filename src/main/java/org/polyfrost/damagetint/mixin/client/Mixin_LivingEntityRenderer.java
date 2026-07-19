package org.polyfrost.damagetint.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.21.4 {
import java.util.Map;
import java.util.WeakHashMap;
//?}

@Mixin(LivingEntityRenderer.class)
public class Mixin_LivingEntityRenderer {

    //? if >=1.21.4 {
    @Unique
    private static final Map<Object, Integer> damageTint$hurtTimeMap = java.util.Collections.synchronizedMap(new WeakHashMap<>());
    @Unique
    private static final Map<Object, Integer> damageTint$deathTimeMap = java.util.Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float f, CallbackInfo ci) {
        damageTint$hurtTimeMap.put(state, entity.hurtTime);
        damageTint$deathTimeMap.put(state, entity.deathTime);
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlayCoords(LivingEntityRenderState state, float f, CallbackInfoReturnable<Integer> cir) {
        if (DamageTintConfig.fade && DamageTintConfig.enabled) {
            Integer hurtTimeObj = damageTint$hurtTimeMap.get(state);
            Integer deathTimeObj = damageTint$deathTimeMap.get(state);
            int hurtTime = hurtTimeObj != null ? hurtTimeObj : 0;
            int deathTime = deathTimeObj != null ? deathTimeObj : 0;

            if (deathTime > 0) {
                if (DamageTintConfig.fadeDeath) {
                    int row = Math.round(((float) deathTime / DamageTintConfig.fadeDuration) * 7.0f);
                    row = Math.max(0, Math.min(7, row));
                    cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), row));
                } else {
                    cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), 0));
                }
            } else if (hurtTime > 0) {
                int row = Math.round((1.0f - (float) hurtTime / DamageTintConfig.fadeDuration) * 7.0f);
                row = Math.max(0, Math.min(7, row));
                cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), row));
            }
        }
    }
    //?} else {
    /*// 1.21.1 has an entirely different method sig/approach
    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlayCoords(LivingEntity entity, float f, CallbackInfoReturnable<Integer> cir) {
        if (DamageTintConfig.fade && DamageTintConfig.enabled) {
            int hurtTime = entity.hurtTime;
            int deathTime = entity.deathTime;

            if (deathTime > 0) {
                if (DamageTintConfig.fadeDeath) {
                    int row = Math.round(((float) deathTime / DamageTintConfig.fadeDuration) * 7.0f);
                    row = Math.max(0, Math.min(7, row));
                    cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), row));
                } else {
                    cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), 0));
                }
            } else if (hurtTime > 0) {
                int row = Math.round((1.0f - (float) hurtTime / DamageTintConfig.fadeDuration) * 7.0f);
                row = Math.max(0, Math.min(7, row));
                cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), row));
            }
        }
    }
    *///?}
}
