package org.polyfrost.damagetint;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DamageTintMixinPlugin implements IMixinConfigPlugin {

    public void onLoad(String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return "";
    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return false;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();

        //#if MC >= 1.16.5
        //$$ mixins.add("client.Mixin_OverlayTexture_SetColor");
        //#else
        mixins.add("client.Mixin_RendererLivingEntity_ModifyGlintColors");
        //#endif

        return mixins;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

}
