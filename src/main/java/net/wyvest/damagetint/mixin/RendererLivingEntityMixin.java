package net.wyvest.damagetint.mixin;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.wyvest.damagetint.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    private EntityLivingBase entitylivingbaseIn;

    @Inject(method = "setBrightness", at = @At("HEAD"))
    private void set(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir) {
        this.entitylivingbaseIn = entitylivingbaseIn;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 0))
    private float getRedTint(float f) {
        if (Config.toggle) {
            if (Config.chroma) {
                return ((float) ((timeBasedChroma() >> 16) & 0xFF)) / 255;
            } else {
                return ((float) Config.color.getRed()) / 255;
            }
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 1))
    private float getGreenTint(float f) {
        if (Config.toggle) {
            if (Config.chroma) {
                return ((float) ((timeBasedChroma() >> 8) & 0xFF)) / 255;
            } else {
                return ((float) Config.color.getGreen()) / 255;
            }
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 2))
    private float getBlueTint(float f) {
        if (Config.toggle) {
            if (Config.chroma) {
                return ((float) (timeBasedChroma() & 0xFF)) / 255;
            } else {
                return ((float) Config.color.getBlue()) / 255;
            }
        }
        return f;
    }

    @ModifyArg(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 3))
    private float getAlphaTint(float f) {
        if (Config.toggle) {
            if (Config.fade) {
                float percent = 1.0F - (float) entitylivingbaseIn.hurtTime / (float) entitylivingbaseIn.maxHurtTime;
                percent = percent < 0.5F ? percent / 0.5F : (1.0F - percent) / 0.5F;
                return (float) Config.color.getAlpha() * percent / 255.0F;
            } else {
                return (float) Config.color.getAlpha() / 255.0F;
            }
        }
        return f;
    }

    public int timeBasedChroma() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % (long) ((int) (10000.0F / (float) Config.speed))) / (10000.0F / (float) Config.speed), 1.0F, 1.0F);
    }
}
