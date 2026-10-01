package org.polyfrost.damagetint.mixin.client;

//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.utils.DamageTintRenderState;
import org.polyfrost.damagetint.client.utils.DamageVariant;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class Mixin_EntityRenderState implements DamageTintRenderState {

    @Unique
    private int damageTint$hurtTime;
    @Unique
    private int damageTint$deathTime;
    @Unique
    private DamageVariant damageTint$variant = DamageVariant.OTHER;

    @Override
    public void damageTint$extract(LivingEntity entity) {
        damageTint$hurtTime = entity.hurtTime;
        damageTint$deathTime = entity.deathTime;
        damageTint$variant = DamageVariantTracker.get(entity);
    }

    @Override
    public int damageTint$getHurtTime() {
        return damageTint$hurtTime;
    }

    @Override
    public int damageTint$getDeathTime() {
        return damageTint$deathTime;
    }

    @Override
    public DamageVariant damageTint$getVariant() {
        return damageTint$variant;
    }
}
//?}
