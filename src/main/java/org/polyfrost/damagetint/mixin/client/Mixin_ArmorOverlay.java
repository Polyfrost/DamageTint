package org.polyfrost.damagetint.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//? if >= 1.21.4
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.Sheets;
//? if >= 1.21.11 {
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
*///?}
//? if >= 1.21.4 {
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
//?} else
//import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.utils.ArmorOverlayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? if >= 1.21.4 {
@Mixin(EquipmentLayerRenderer.class)
//?} else {
/*@Mixin(HumanoidArmorLayer.class)*/
//?}
public class Mixin_ArmorOverlay {

    //? if >= 1.21.4 {
    @Unique
    private static boolean damageTint$shouldTint(EquipmentClientInfo.LayerType layerType) {
        boolean humanoidArmor = layerType == EquipmentClientInfo.LayerType.HUMANOID
                || layerType == EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS;
        //? if >= 26
        humanoidArmor |= layerType == EquipmentClientInfo.LayerType.HUMANOID_BABY;
        return ArmorOverlayContext.isActive() && humanoidArmor;
    }
    //?} else {
    /*@Unique
    private static boolean damageTint$shouldTint() {
        return ArmorOverlayContext.isActive();
    }
    *///?}

    //? if >= 1.21.11 {
    @WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;")
    )
    private RenderType damageTint$armorRenderType(Identifier texture, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(texture);
        if (!damageTint$shouldTint(layerType) || current != RenderTypes.armorCutoutNoCull(texture)) {
            return current;
        }
        //? if >= 26 {
        return RenderTypes.entityCutoutZOffset(texture);
        //?} else {
        /*return RenderTypes.entityCutoutNoCullZOffset(texture);*/
        //?}
    }
    //?} elif >= 1.21.10 {
    /*@WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$armorRenderType(ResourceLocation texture, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(texture);
        return damageTint$shouldTint(layerType) && current == RenderType.armorCutoutNoCull(texture)
                ? RenderType.entityCutoutNoCullZOffset(texture)
                : current;
    }
    *///?} elif >= 1.21.4 {
    /*@WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$armorRenderType(ResourceLocation texture, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(texture);
        return damageTint$shouldTint(layerType) && current == RenderType.armorCutoutNoCull(texture)
                ? RenderType.entityCutoutNoCullZOffset(texture)
                : current;
    }
    *///?} else {
    /*@WrapOperation(
            method = "renderModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$armorRenderType(ResourceLocation texture, Operation<RenderType> original) {
        RenderType current = original.call(texture);
        return damageTint$shouldTint() && current == RenderType.armorCutoutNoCull(texture)
                ? RenderType.entityCutoutNoCullZOffset(texture)
                : current;
    }
    *///?}

    //? if >= 1.21.11 {
    @WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/rendertype/RenderType;")
    )
    private RenderType damageTint$trimRenderType(boolean decal, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(decal);
        if (!damageTint$shouldTint(layerType) || decal || current != Sheets.armorTrimsSheet(decal)) {
            return current;
        }
        //? if >= 26 {
        return RenderTypes.entityCutoutZOffset(Sheets.ARMOR_TRIMS_SHEET);
        //?} else {
        /*return RenderTypes.entityCutoutNoCullZOffset(Sheets.ARMOR_TRIMS_SHEET);*/
        //?}
    }
    //?} elif >= 1.21.10 {
    /*@WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$trimRenderType(boolean decal, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(decal);
        return damageTint$shouldTint(layerType) && !decal && current == Sheets.armorTrimsSheet(decal)
                ? RenderType.entityCutoutNoCullZOffset(Sheets.ARMOR_TRIMS_SHEET)
                : current;
    }
    *///?} elif >= 1.21.4 {
    /*@WrapOperation(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$trimRenderType(boolean decal, Operation<RenderType> original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        RenderType current = original.call(decal);
        return damageTint$shouldTint(layerType) && !decal && current == Sheets.armorTrimsSheet(decal)
                ? RenderType.entityCutoutNoCullZOffset(Sheets.ARMOR_TRIMS_SHEET)
                : current;
    }
    *///?} else {
    /*@WrapOperation(
            method = "renderTrim",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Sheets;armorTrimsSheet(Z)Lnet/minecraft/client/renderer/RenderType;")
    )
    private RenderType damageTint$trimRenderType(boolean decal, Operation<RenderType> original) {
        RenderType current = original.call(decal);
        return damageTint$shouldTint() && !decal && current == Sheets.armorTrimsSheet(decal)
                ? RenderType.entityCutoutNoCullZOffset(Sheets.ARMOR_TRIMS_SHEET)
                : current;
    }
    *///?}

    //? if >= 1.21.10 {
    @ModifyExpressionValue(
            //? if >= 1.21.11 {
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            //?} else {
            /*method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V",*/
            //?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I", ordinal = 0)
    )
    private int damageTint$baseArmorOverlay(int original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        return damageTint$shouldTint(layerType) ? ArmorOverlayContext.applyTo(original) : original;
    }

    @ModifyExpressionValue(
            //? if >= 1.21.11 {
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            //?} else {
            /*method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V",*/
            //?}
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I", ordinal = 2)
    )
    private int damageTint$trimOverlay(int original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        return damageTint$shouldTint(layerType) ? ArmorOverlayContext.applyTo(original) : original;
    }
    //?} elif >= 1.21.4 {
    /*@ModifyExpressionValue(
            method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I")
    )
    private int damageTint$armorOverlay(int original, @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType) {
        return damageTint$shouldTint(layerType) ? ArmorOverlayContext.applyTo(original) : original;
    }
    *///?} else {
    /*@ModifyExpressionValue(
            method = {"renderModel", "renderTrim"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/OverlayTexture;NO_OVERLAY:I")
    )
    private int damageTint$armorOverlay(int original) {
        return ArmorOverlayContext.applyTo(original);
    }
    *///?}
}
