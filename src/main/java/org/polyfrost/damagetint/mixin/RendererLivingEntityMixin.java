package org.polyfrost.damagetint.mixin;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.polyfrost.damagetint.DamageTint;
import org.polyfrost.damagetint.config.DamageTintConfig;
import org.polyfrost.polyui.utils.ColorUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    @Unique
    private EntityLivingBase damageTint$entitylivingbaseIn;

    @Inject(method = "setBrightness", at = @At("HEAD"))
    private void set(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir) {
        damageTint$entitylivingbaseIn = entitylivingbaseIn;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 0))
    private float getRedTint(float f) {
        if (DamageTint.config.enabled) {
            return ((float) ColorUtils.getRed(DamageTintConfig.color.getArgb())) / 255f;
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 1))
    private float getGreenTint(float f) {
        if (DamageTint.config.enabled) {
            return ((float) ColorUtils.getGreen(DamageTintConfig.color.getArgb())) / 255f;
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 2))
    private float getBlueTint(float f) {
        if (DamageTint.config.enabled) {
            return ((float) ColorUtils.getBlue(DamageTintConfig.color.getArgb())) / 255f;
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 3))
    private float getAlphaTint(float f) {
        if (DamageTint.config.enabled) {
            if (DamageTintConfig.fade) {
                float percent = 1.0F - (float) damageTint$entitylivingbaseIn.hurtTime / (float) damageTint$entitylivingbaseIn.maxHurtTime;
                percent = percent < 0.5F ? percent / 0.5F : (1.0F - percent) / 0.5F;
                return (float) ColorUtils.getAlpha(DamageTintConfig.color.getArgb()) * percent / 255.0F;
            } else {
                return (float) ColorUtils.getAlpha(DamageTintConfig.color.getArgb()) / 255.0F;
            }
        }
        return f;
    }
}
