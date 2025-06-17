package org.polyfrost.damagetint.mixin.client;

//#if MC >= 1.16.5
//$$ import com.mojang.blaze3d.platform.NativeImage;
//$$ import dev.deftu.omnicore.client.render.OmniImage;
//$$ import net.minecraft.client.renderer.texture.DynamicTexture;
//$$ import net.minecraft.client.renderer.texture.OverlayTexture;
//$$ import org.polyfrost.damagetint.client.utils.OverlayModifier;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.Unique;
//$$
//$$ @Mixin(OverlayTexture.class)
//$$ public class Mixin_OverlayTexture_SetColor implements OverlayModifier {
//$$
//$$     @Shadow @Final
//$$     private DynamicTexture texture;
//$$
//$$     @Unique
//$$     @Override
//$$     public void setOverlayColor(int color) {
//$$         NativeImage image = this.texture.getPixels();
//$$         if (image == null) {
//$$             throw new IllegalStateException("Overlay texture's image is null");
//$$         }
//$$
//$$         OmniImage oldImage = OmniImage.from(image); // Allocate an OmniImage from the old image, which will be retained for pixel data
//$$         OmniImage newImage = OmniImage.from(image); // This is our new image which we will modify and use for the texture
//$$         for (int x = 0; x < newImage.getWidth(); x++) {
//$$             for (int y = 0; y < newImage.getHeight(); y++) {
//$$                 if (y < newImage.getHeight() / 2) {
//$$                     // Set the top half of the image to the new color, overriding the original color
//$$                     newImage.setPixel(x, y, color);
//$$                 } else {
//$$                     // Keep the bottom half of the image unchanged. I can't remember why this is necessary, but it is, otherwise the overlay texture breaks completely
//$$                     newImage.setPixel(x, y, oldImage.getPixel(x, y));
//$$                 }
//$$             }
//$$         }
//$$
//$$         oldImage.close(); // Close the old image to free resources. We were only using it for quick access to the original pixel data
//$$
//$$         // Now we can set the new image to the texture
//$$         NativeImage nativeImage = newImage.getNative();
//$$         this.texture.setPixels(nativeImage);
//$$         this.texture.upload();
//$$         nativeImage.close(); // Close the original image to free resources. No memory leaks here!
//$$     }
//$$
//$$ }
//#endif
