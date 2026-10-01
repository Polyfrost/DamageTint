package org.polyfrost.damagetint.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
//?}
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

//? if =1.8.9 {
/*import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.FloatBuffer;
*///?}

@Mixin(LivingEntityRenderer.class)
public class Mixin_LivingEntityRenderer {

    //? if >=1.21.4 {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void onExtractRenderState(LivingEntity entity, LivingEntityRenderState state, float f, CallbackInfo ci) {
        ((DamageTintRenderState) state).damageTint$extract(entity);
    }

    @ModifyReturnValue(method = "getOverlayCoords", at = @At("RETURN"))
    private static int onGetOverlayCoords(int original, LivingEntityRenderState state) {
        DamageTintRenderState data = (DamageTintRenderState) state;
        return damageTint$overlayCoords(original, data.damageTint$getHurtTime(), data.damageTint$getDeathTime(), data.damageTint$getVariant());
    }
    //?} elif >1.8.9 {
    /*@ModifyReturnValue(method = "getOverlayCoords", at = @At("RETURN"))
    private static int onGetOverlayCoords(int original, LivingEntity entity) {
        return damageTint$overlayCoords(original, entity.hurtTime, entity.deathTime, DamageVariantTracker.get(entity));
    }
    *///?} else {
    /*@Shadow
    protected FloatBuffer tintBuffer;

    @Inject(
            method = "setupOverlayColor(Lnet/minecraft/world/entity/LivingEntity;FZ)Z",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTexEnv(IILjava/nio/FloatBuffer;)V")
    )
    private void damageTint$modifyTint(LivingEntity entity, float tickDelta, boolean alwaysRender, CallbackInfoReturnable<Boolean> cir) {
        if (!DamageTintConfig.enabled || entity.hurtTime == 0 && entity.deathTime == 0) {
            return;
        }

        int argb = DamageTintConfig.colorFor(DamageVariantTracker.get(entity)).getArgb();
        float alpha = (argb >>> 24) / 255.0f;
        float r = (argb >> 16 & 0xFF) / 255.0f;
        float g = (argb >> 8 & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        if (DamageTintConfig.fade) {
            if (entity.deathTime > 0 && DamageTintConfig.fadeDeath) {
                alpha *= Math.clamp(1.0f - entity.deathTime / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
            } else if (entity.deathTime == 0) {
                alpha *= Math.clamp(entity.hurtTime / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
            }
        }

        this.tintBuffer.put(0, r);
        this.tintBuffer.put(1, g);
        this.tintBuffer.put(2, b);
        this.tintBuffer.put(3, alpha);
    }
    *///?}

    @Unique
    private static int damageTint$overlayCoords(int original, int hurtTime, int deathTime, DamageVariant variant) {
        boolean hasRedOverlay = hurtTime > 0 || deathTime > 0;
        int coords = OverlayCoords.of(hasRedOverlay, hurtTime, deathTime, variant, original & 0xFFFF);
        return coords == OverlayCoords.NO_OVERRIDE ? original : coords;
    }
}
