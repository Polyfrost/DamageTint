package xyz.qalcyo.damagetint.mixin;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import xyz.qalcyo.damagetint.config.Config;

import java.awt.*;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    private boolean flag = false;
    private int count;
    private EntityLivingBase entitylivingbaseIn;

    @Inject(method = "setBrightness", at = @At("HEAD"))
    private void set(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir) {
        flag = (entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0) && Config.toggle;
        count = 0;
        this.entitylivingbaseIn = entitylivingbaseIn;
    }

    @ModifyArgs(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;"))
    private void returnColor(Args args) {
        if (flag) {
            float arg = args.get(0);
            if (arg == 1.0F) {
                //red
                if (Config.chroma) {
                    args.set(0, ((float) ((timeBasedChroma() >> 16) & 0xFF)) / 255);
                } else {
                    args.set(0, ((float) Config.color.getRed()) / 255);
                }
            } else if (arg == 0.0F) {
                //blue / green
                ++count;
                if (count == 1) {
                    if (Config.chroma) {
                        args.set(0, ((float) ((timeBasedChroma() >> 8) & 0xFF)) / 255);
                    } else {
                        args.set(0, ((float) Config.color.getGreen()) / 255);
                    }
                } else {
                    if (Config.chroma) {
                        args.set(0, ((float) ((timeBasedChroma()) & 0xFF)) / 255);
                    } else {
                        args.set(0, ((float) Config.color.getBlue()) / 255);
                    }
                }
            } else {
                if (Config.fade) {
                    float percent = 1.0F - (float) entitylivingbaseIn.hurtTime / (float) entitylivingbaseIn.maxHurtTime;
                    percent = percent < 0.5F ? percent / 0.5F : (1.0F - percent) / 0.5F;
                    args.set(0, (float) Config.color.getAlpha() * percent / 255.0F);
                } else {
                    args.set(0, (float) Config.color.getAlpha() / 255.0F);
                }
            }
        }
    }

    public int timeBasedChroma() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % (long) ((int) (10000.0F / (float) Config.speed))) / (10000.0F / (float) Config.speed), 1.0F, 1.0F);
    }
}
