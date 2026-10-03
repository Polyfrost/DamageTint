package org.polyfrost.damagetint.mixin.client;

//? if >1.8.9
import com.mojang.blaze3d.vertex.PoseStack;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//? if >= 1.21.10 {
import net.minecraft.client.renderer.SubmitNodeCollector;
//~ if < 26 'state.level.CameraRenderState' -> 'state.CameraRenderState'
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?} elif >1.8.9
//import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
//? if >= 1.21.4 {
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import org.polyfrost.damagetint.client.utils.DamageTintRenderState;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.polyfrost.damagetint.client.utils.DamageVariant;
//? if <1.21.4 {
/*import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
*///?}
import org.polyfrost.damagetint.client.utils.OverlayCoords;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? if =1.8.9 {
/*import net.minecraft.client.render.platform.GlStateManager;
import org.polyfrost.damagetint.client.DamageTintConfig;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Mixin(EnderDragonRenderer.class)
public class Mixin_EnderDragonRenderer {

    //? if >= 1.21.4 {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At("HEAD"))
    private void damageTint$recordHurtTime(EnderDragon entity, EnderDragonRenderState state, float partialTicks, CallbackInfo ci) {
        ((DamageTintRenderState) state).damageTint$extract(entity);
    }
    //?}

    //? if >= 1.21.10 {
    @ModifyExpressionValue(
            //~ if < 26 'state/level/CameraRenderState' -> 'state/CameraRenderState'
            method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        return damageTint$overlayCoords(original, state);
    }
    //?} elif >= 1.21.4 {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragonRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$overlayCoords(original, state);
    }
    *///?} elif >1.8.9 {
    /*@ModifyExpressionValue(
            method = "render(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;pack(FZ)I")
    )
    private int damageTint$getOverlayCoords(int original, EnderDragon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        return damageTint$overlayCoords(original, entity.hurtTime > 0, entity.hurtTime, DamageVariantTracker.get(entity));
    }
    *///?} else {
    /*@Inject(
            method = "renderModel(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/platform/GlStateManager;color4f(FFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void damageTint$modifyTint(EnderDragon entity, float limbAngle, float limbDistance, float tickDelta, float yaw, float pitch, float scale, CallbackInfo ci) {
        if (!DamageTintConfig.enabled) {
            return;
        }

        int argb = DamageTintConfig.colorFor(DamageVariantTracker.get(entity)).getArgb();
        float alpha = (argb >>> 24) / 255.0f;
        float r = (argb >> 16 & 0xFF) / 255.0f;
        float g = (argb >> 8 & 0xFF) / 255.0f;
        float b = (argb & 0xFF) / 255.0f;

        if (DamageTintConfig.fade) {
            alpha *= Math.clamp(entity.hurtTime / DamageTintConfig.fadeDuration, 0.0f, 1.0f);
        }

        GlStateManager.color4f(r, g, b, alpha);
    }
    *///?}

    //? if >= 1.21.4 {
    @Unique
    private int damageTint$overlayCoords(int original, EnderDragonRenderState state) {
        DamageTintRenderState data = (DamageTintRenderState) state;
        return damageTint$overlayCoords(original, state.hasRedOverlay, data.damageTint$getHurtTime(), data.damageTint$getVariant());
    }
    //?}

    @Unique
    private static int damageTint$overlayCoords(int original, boolean hasRedOverlay, int hurtTime, DamageVariant variant) {
        int coords = OverlayCoords.of(hasRedOverlay, hurtTime, 0, variant, original & 0xFFFF);
        return coords == OverlayCoords.NO_OVERRIDE ? original : coords;
    }
}
