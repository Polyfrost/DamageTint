package org.polyfrost.damagetint.mixin.client;

//? if >1.8.9 {
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class Mixin_ModifyTintColor implements OverlayModifier {

    @Shadow
    @Final
    private DynamicTexture texture;

    @Unique
    private int damageTint$originalPixel;

    // Other mods can replace the overlay texture or change its vanilla color
    @Inject(method = "<init>", at = @At("TAIL"))
    private void damageTint$applyConfiguredColors(CallbackInfo ci) {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            return;
        }
        //? if >=1.21.4 {
        damageTint$originalPixel = image.getPixel(0, 0);
        //?} else {
        /*damageTint$originalPixel = image.getPixelRGBA(0, 0);
        *///?}
        if (DamageTintConfig.enabled) {
            DamageTintConfig.applyOverlayColors(this);
        }
    }

    @Override
    @Unique
    public void damageTint$restoreOverlayColors() {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight() / 2; y++) {
                //? if >=1.21.4 {
                image.setPixel(x, y, damageTint$originalPixel);
                //?} else {
                /*image.setPixelRGBA(x, y, damageTint$originalPixel);
                *///?}
            }
        }

        this.texture.upload();
    }

    @Override
    @Unique
    public void damageTint$setOverlayColors(int[] argbByColumn, boolean fade) {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }

        int rows = image.getHeight() / 2;
        for (int x = 0; x < image.getWidth(); x++) {
            int argb = argbByColumn[Math.min(x, argbByColumn.length - 1)];
            int r = argb >> 16 & 0xFF;
            int g = argb >> 8 & 0xFF;
            int b = argb & 0xFF;
            int a = 255 - (argb >>> 24);

            for (int y = 0; y < rows; y++) {
                float percent = fade ? 1.0f - ((float) y / 7.0f) : 1.0f;
                int currentAlpha = (int) (255 - ((255 - a) * percent));
                //? if >=1.21.4 {
                int packedColor = (currentAlpha << 24 | r << 16 | g << 8 | b);
                image.setPixel(x, y, packedColor);
                //?} else {
                /*int packedColor = (currentAlpha << 24 | b << 16 | g << 8 | r);
                image.setPixelRGBA(x, y, packedColor);
                *///?}
            }
        }

        this.texture.upload();
    }
}
//?}
