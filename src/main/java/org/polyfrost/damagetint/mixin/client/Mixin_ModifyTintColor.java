package org.polyfrost.damagetint.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import dev.deftu.omnicore.api.client.image.OmniImage;
import dev.deftu.omnicore.api.client.image.OmniImages;
import dev.deftu.omnicore.api.color.OmniColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.DamageTintClient;
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
    public void damageTint$setOverlayColor(OmniColor color) {
        NativeImage image = this.texture.getPixels();
        if (image == null) {
            throw new IllegalStateException("Overlay texture's image is null");
        }

        OmniImage oldImage = OmniImages.from(image);
        OmniImage newImage = OmniImages.from(image);
        DamageTintClient.INSTANCE.LOGGER.info("updateOverlayColor called in mixin");

        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                if (y < newImage.getHeight() / 2) {
                    newImage.set(x, y, color);
                } else {
                    newImage.set(x, y, oldImage.get(x, y));
                }
            }
        }

        try {
            oldImage.close();
        } catch (Throwable ignored) {}

        NativeImage nativeImage = newImage.getNative();
        this.texture.setPixels(nativeImage);
        this.texture.upload();
        image.close();
    }
}