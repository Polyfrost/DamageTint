package org.polyfrost.damagetint.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DamageTintMixinPlugin implements IMixinConfigPlugin {

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();
        //? if >1.8.9 {
        mixins.add("client.Mixin_ModifyTintColor");
        //?}
        //? if >=1.21.4 {
        mixins.add("client.Mixin_EntityRenderState");
        //?}
        return mixins;
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
