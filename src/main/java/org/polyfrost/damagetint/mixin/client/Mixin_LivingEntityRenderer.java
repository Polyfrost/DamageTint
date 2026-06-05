package org.polyfrost.damagetint.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.WeakHashMap;

@Mixin(LivingEntityRenderer.class)
public class Mixin_LivingEntityRenderer {

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
            
            if (hurtTime > 0) {
                float percent = 1.0F - (float) hurtTime / 10.0F;
                percent = percent < 0.5F ? percent / 0.5F : (1.0F - percent) / 0.5F;
                int row = Math.round((1.0f - percent) * 7.0f);
                cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), row));
            } else if (deathTime > 0) {
                cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(f), 0));
            }
        }
    }
}
