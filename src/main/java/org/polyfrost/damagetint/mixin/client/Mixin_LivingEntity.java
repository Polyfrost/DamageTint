package org.polyfrost.damagetint.mixin.client;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.utils.DamageVariant;
import org.polyfrost.damagetint.client.utils.DamageVariantHolder;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class Mixin_LivingEntity implements DamageVariantHolder {

    @Unique
    private DamageVariant damageTint$variant;
    @Unique
    private int damageTint$hurtTick;

    //? if >1.8.9 {
    @Inject(method = "handleDamageEvent", at = @At("HEAD"))
    private void damageTint$recordDamageVariant(DamageSource source, CallbackInfo ci) {
        DamageVariantTracker.record((LivingEntity) (Object) this, source);
    }
    //?}

    @Override
    public DamageVariant damageTint$getVariant() {
        return damageTint$variant;
    }

    @Override
    public void damageTint$setVariant(DamageVariant variant) {
        damageTint$variant = variant;
    }

    @Override
    public int damageTint$getHurtTick() {
        return damageTint$hurtTick;
    }

    @Override
    public void damageTint$setHurtTick(int tick) {
        damageTint$hurtTick = tick;
    }
}
