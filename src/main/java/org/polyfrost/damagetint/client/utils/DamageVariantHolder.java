package org.polyfrost.damagetint.client.utils;

public interface DamageVariantHolder {
    // Null until the entity's first damage event.
    DamageVariant damageTint$getVariant();

    void damageTint$setVariant(DamageVariant variant);

    int damageTint$getHurtTick();

    void damageTint$setHurtTick(int tick);
}
