package org.polyfrost.damagetint.mixin.client;

//? if > 1.8.9 {
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?} else {
/*import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Shadow;
import java.nio.FloatBuffer;
*///?}
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    //?} elif > 1.8.9 {
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
    *///?} else {
    /*@Shadow
    protected FloatBuffer tintBuffer;

    @Inject(
            method = "setupOverlayColor(Lnet/minecraft/entity/living/LivingEntity;FZ)Z",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTexEnv(IILjava/nio/FloatBuffer;)V")
    )
    private void damageTint$modifyTint(LivingEntity entity, float tickDelta, boolean alwaysRender, CallbackInfoReturnable<Boolean> cir) {
        if (!DamageTintConfig.enabled || entity.damagedTimer == 0 && entity.deathTicks == 0) {
            return;
        }

        int argb = DamageTintConfig.colorV2.getArgb();
        float alpha = (argb >>> 24) / 255.0f;
        float r = (argb >> 16 & 0xFF) / 255.0f;
        float g = (argb >> 8 & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        if (DamageTintConfig.fade) {
            if (entity.deathTicks > 0 && DamageTintConfig.fadeDeath) {
                alpha *= Math.clamp(1.0f - entity.deathTicks / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
            } else if (entity.deathTicks == 0) {
                alpha *= Math.clamp(entity.damagedTimer / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
            }
        }

        this.tintBuffer.put(0, r);
        this.tintBuffer.put(1, g);
        this.tintBuffer.put(2, b);
        this.tintBuffer.put(3, alpha);
    }
    *///?}
}
