package org.polyfrost.damagetint.client.utils;

//? if >1.8.9 {
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
//?}
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
//? if >1.8.9
import net.minecraft.world.entity.projectile.Projectile;
//? if >1.8.9 && <1.21.4
//import net.minecraft.world.item.MaceItem;

//? if =1.8.9 {
/*import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
*///?}

public final class DamageVariantTracker {

    private static final int CRIT_WINDOW_TICKS = 1;

    private DamageVariantTracker() {
    }

    //? if >1.8.9 {
    public static void record(LivingEntity entity, DamageSource source) {
        DamageVariantHolder holder = (DamageVariantHolder) entity;
        holder.damageTint$setVariant(classify(source));
        holder.damageTint$setHurtTick(entity.tickCount);
    }
    //?} else {
    /*public static void record(LivingEntity entity) {
        DamageVariantHolder holder = (DamageVariantHolder) entity;
        holder.damageTint$setVariant(isBlocking(entity) ? DamageVariant.BLOCK : DamageVariant.OTHER);
        holder.damageTint$setHurtTick(entity.tickCount);
    }

    // isSwordBlocking reads itemInUse, which RemoteClientPlayerEntity only rebuilds once a tick and
    // so can be stale here; the synced flag is current but is never set for our own player.
    private static boolean isBlocking(LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        if (player.isSwordBlocking()) {
            return true;
        }

        ItemStack held = player.inventory.getSelectedItem();
        return player.isUsingItem() && held != null && held.getUseAction() == UseAction.BLOCK;
    }
    *///?}

    public static void recordCrit(LivingEntity entity) {
        DamageVariantHolder holder = (DamageVariantHolder) entity;
        DamageVariant variant = holder.damageTint$getVariant();
        if (variant == null || entity.tickCount - holder.damageTint$getHurtTick() > CRIT_WINDOW_TICKS) {
            return;
        }

        if (entity.hurtTime > 0 && variant != DamageVariant.MACE && variant != DamageVariant.BLOCK) {
            holder.damageTint$setVariant(DamageVariant.CRIT);
        }
    }

    public static DamageVariant get(LivingEntity entity) {
        DamageVariant variant = ((DamageVariantHolder) entity).damageTint$getVariant();
        return variant != null ? variant : DamageVariant.OTHER;
    }

    //? if >1.8.9 {
    private static DamageVariant classify(DamageSource source) {
        //? if >=1.21.4 {
        if (source.is(DamageTypeTags.IS_MACE_SMASH)) {
            return DamageVariant.MACE;
        }
        //?} else {
        /*if (isSmashAttack(source)) {
            return DamageVariant.MACE;
        }
        *///?}

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            return DamageVariant.RANGED;
        }

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return DamageVariant.EXPLOSION;
        }

        if (source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(DamageTypes.SONIC_BOOM)
                || source.is(DamageTypes.DRAGON_BREATH)
                || source.is(DamageTypes.WITHER)) {
            return DamageVariant.MAGIC;
        }

        if (source.is(DamageTypeTags.IS_PLAYER_ATTACK)
                || source.is(DamageTypes.MOB_ATTACK)
                || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
            return DamageVariant.MELEE;
        }

        return classifyByEntity(source);
    }

    private static DamageVariant classifyByEntity(DamageSource source) {
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile) {
            return DamageVariant.RANGED;
        }

        if (direct instanceof LivingEntity && direct == source.getEntity()) {
            return DamageVariant.MELEE;
        }

        return DamageVariant.OTHER;
    }
    //?}

    //? if >1.8.9 && <1.21.4 {
    /*private static boolean isSmashAttack(DamageSource source) {
        return source.getEntity() instanceof LivingEntity attacker
                && attacker.getWeaponItem().getItem() instanceof MaceItem
                && MaceItem.canSmashAttack(attacker);
    }
    *///?}
}
