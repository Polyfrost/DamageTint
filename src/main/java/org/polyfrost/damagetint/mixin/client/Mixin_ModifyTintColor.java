package org.polyfrost.damagetint.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.DamageTintClient;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Method;

@Mixin(OverlayTexture.class)
public class Mixin_ModifyTintColor implements OverlayModifier {

    @Shadow
    @Final
    private DynamicTexture texture;

    @Override
    @Unique
    public void damageTint$setOverlayColor(int a, int r, int g, int b) {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }

        boolean isARGB = true;
        Method setPixelMethod = null;
        try {
            setPixelMethod = NativeImage.class.getMethod("setPixel", int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            try {
                setPixelMethod = NativeImage.class.getMethod("setPixelRGBA", int.class, int.class, int.class);
                isARGB = false;
            } catch (NoSuchMethodException e2) {
                DamageTintClient.INSTANCE.LOGGER.error("Fatal: could not find setPixel or setPixelRGBA");
            }
        }

        if (setPixelMethod != null) {
            for (int y = 0; y < image.getHeight() / 2; y++) {
                float percent = 1.0f - ((float) y / 7.0f);
                int currentAlpha = (int) (255 - ((255 - a) * percent));
                int packedColor = isARGB 
                    ? (currentAlpha << 24 | r << 16 | g << 8 | b) 
                    : (currentAlpha << 24 | b << 16 | g << 8 | r);

                for (int x = 0; x < image.getWidth(); x++) {
                    try {
                        setPixelMethod.invoke(image, x, y, packedColor);
                    } catch (Exception e) {}
                }
            }
        }

        this.texture.upload();
    }
}