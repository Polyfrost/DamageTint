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

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    private boolean flag = false;
    private int count;
    @Inject(method = "setBrightness", at = @At("HEAD"))
    private void set(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir) {
        flag = (entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0) && Config.toggle;
        count = 0;
    }
    @ModifyArgs(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;"))
    private void returnColor(Args args) {
        if (flag) {
            float arg = args.get(0);
            if (arg == 1.0F) {
                //red
                args.set(0, ((float) Config.color.getRed()) / 255);
            } else if (arg == 0.0F) {
                //blue / green
                ++count;
                if (count == 1) {
                    args.set(0, ((float) Config.color.getGreen()) / 255);
                } else {
                    args.set(0, ((float) Config.color.getBlue()) / 255);
                }
            }
        }
    }
}
