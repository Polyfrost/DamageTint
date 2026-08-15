package org.polyfrost.damagetint.mixin.client;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class Mixin_LivingEntity {

    @Inject(method = "handleDamageEvent", at = @At("HEAD"))
    private void damageTint$recordDamageVariant(DamageSource source, CallbackInfo ci) {
        DamageVariantTracker.record((LivingEntity) (Object) this, source);
    }
}
