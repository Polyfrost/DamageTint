package org.polyfrost.damagetint.mixin.client;

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

    @Override
    @Unique
    public void damageTint$setOverlayColor(int a, int r, int g, int b) {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }

        for (int y = 0; y < image.getHeight() / 2; y++) {
            float percent = 1.0f - ((float) y / 7.0f);
            int currentAlpha = (int) (255 - ((255 - a) * percent));
            //? if >=1.21.4 {
            int packedColor = (currentAlpha << 24 | r << 16 | g << 8 | b);
            //?} else {
            /*int packedColor = (currentAlpha << 24 | b << 16 | g << 8 | r);
            *///?}

            for (int x = 0; x < image.getWidth(); x++) {
                //? if >=1.21.4 {
                image.setPixel(x, y, packedColor);
                //?} else {
                /*image.setPixelRGBA(x, y, packedColor);
                *///?}
            }
        }

        this.texture.upload();
    }
}