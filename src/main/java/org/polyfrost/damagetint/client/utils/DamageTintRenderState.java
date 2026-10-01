package org.polyfrost.damagetint.client.utils;

import net.minecraft.world.entity.LivingEntity;

public interface DamageTintRenderState {
    void damageTint$extract(LivingEntity entity);

    int damageTint$getHurtTime();

    int damageTint$getDeathTime();

    DamageVariant damageTint$getVariant();
}
