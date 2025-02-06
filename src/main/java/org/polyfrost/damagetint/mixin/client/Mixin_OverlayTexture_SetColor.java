package org.polyfrost.damagetint.mixin.client;

//#if MC >= 1.16.5
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import net.minecraft.client.render.OverlayTexture;
//$$ import net.minecraft.client.texture.NativeImage;
//$$ import net.minecraft.client.texture.NativeImageBackedTexture;
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
//$$     private NativeImageBackedTexture texture;
//$$
//$$     @Unique
//$$     @Override
//$$     public void setOverlayColor(int color) {
//$$         NativeImage image = this.texture.getImage();
//$$         if (image == null) {
//$$             throw new IllegalStateException("Overlay texture's image is null");
//$$         }
//$$
//$$         NativeImage newImage = new NativeImage(image.getWidth(), image.getHeight(), false);
//$$         for (int x = 0; x < newImage.getWidth(); x++) {
//$$             for (int y = 0; y < newImage.getHeight(); y++) {
//$$                 if (y < newImage.getHeight() / 2) {
//$$                     newImage.setPixelColor(x, y, color);
//$$                 } else {
//$$                     newImage.setPixelColor(x, y, image.getPixelColor(x, y));
//$$                 }
//$$             }
//$$         }
//$$
//$$         this.texture.setImage(newImage);
//$$         RenderSystem.activeTexture(33985);
//$$         this.texture.bindTexture();
//$$         newImage.upload(0, 0, 0, 0, 0, newImage.getWidth(), newImage.getHeight(), false, true, false, false);
//$$         RenderSystem.activeTexture(33984);
//$$     }
//$$
//$$ }
//#endif
