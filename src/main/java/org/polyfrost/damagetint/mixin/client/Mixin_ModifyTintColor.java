package org.polyfrost.damagetint.mixin.client;

//? if >1.8.9 {
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OverlayTexture.class)
public class Mixin_ModifyTintColor implements OverlayModifier {

    @Shadow
    @Final
    private DynamicTexture texture;

    // Per-pixel snapshot of the top half before DamageTint wrote it, or null when DamageTint has not tinted
    @Unique
    private int[] damageTint$original;

    // Pixels DamageTint last wrote. Colour or alpha that no longer matches was changed by another mod and are left alone.
    @Unique
    private int[] damageTint$written;

    @Override
    @Unique
    public void damageTint$restoreOverlayColors() {
        if (damageTint$written == null) {
            return;
        }
        NativeImage image = damageTint$image();
        int width = image.getWidth();
        for (int y = 0; y < image.getHeight() / 2; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                damageTint$setPixel(image, x, y, damageTint$merge(damageTint$getPixel(image, x, y), damageTint$written[i], damageTint$original[i]));
            }
        }
        damageTint$original = null;
        damageTint$written = null;

        this.texture.upload();
    }

    @Override
    @Unique
    public void damageTint$setOverlayColors(int[] argbByColumn, boolean fade) {
        NativeImage image = damageTint$image();
        int width = image.getWidth();
        int rows = image.getHeight() / 2;
        boolean owned = damageTint$written != null;
        if (!owned) {
            damageTint$original = new int[width * rows];
            damageTint$written = new int[width * rows];
        }

        for (int x = 0; x < width; x++) {
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
                //?} else {
                /*int packedColor = (currentAlpha << 24 | b << 16 | g << 8 | r);
                *///?}
                int i = y * width + x;
                int current = damageTint$getPixel(image, x, y);
                // Re-snapshot channels another mod changed since DamageTint's last write
                damageTint$original[i] = owned ? damageTint$merge(current, damageTint$written[i], damageTint$original[i]) : current;
                damageTint$setPixel(image, x, y, packedColor);
                damageTint$written[i] = packedColor;
            }
        }

        this.texture.upload();
    }

    // A pixel whose colour another mod changed is kept whole. If only its alpha changed, the original colour comes back
    // with the mod's alpha.
    @Unique
    private static int damageTint$merge(int current, int written, int original) {
        if (((current ^ written) & 0x00FFFFFF) != 0) {
            return current;
        }
        return (original & 0x00FFFFFF) | ((current & 0xFF000000) == (written & 0xFF000000) ? original : current) & 0xFF000000;
    }

    @Unique
    private NativeImage damageTint$image() {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }
        return image;
    }

    @Unique
    private static int damageTint$getPixel(NativeImage image, int x, int y) {
        //? if >=1.21.4 {
        return image.getPixel(x, y);
        //?} else {
        /*return image.getPixelRGBA(x, y);
        *///?}
    }

    @Unique
    private static void damageTint$setPixel(NativeImage image, int x, int y, int color) {
        //? if >=1.21.4 {
        image.setPixel(x, y, color);
        //?} else {
        /*image.setPixelRGBA(x, y, color);
        *///?}
    }
}
//?}
