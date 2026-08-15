package org.polyfrost.damagetint.client.utils;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
//? if <1.21.4
//import net.minecraft.world.item.MaceItem;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class DamageVariantTracker {

    private static final Map<LivingEntity, DamageVariant> VARIANTS = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<LivingEntity, Integer> HURT_TICKS = Collections.synchronizedMap(new WeakHashMap<>());

    private static final int CRIT_WINDOW_TICKS = 1;

    private DamageVariantTracker() {
    }

    public static void record(LivingEntity entity, DamageSource source) {
        VARIANTS.put(entity, classify(source));
        HURT_TICKS.put(entity, entity.tickCount);
    }

    public static void recordCrit(LivingEntity entity) {
        Integer hurtTick = HURT_TICKS.get(entity);
        if (hurtTick == null || entity.tickCount - hurtTick > CRIT_WINDOW_TICKS) {
            return;
        }

        if (entity.hurtTime > 0 && get(entity) != DamageVariant.MACE) {
            VARIANTS.put(entity, DamageVariant.CRIT);
        }
    }

    public static DamageVariant get(LivingEntity entity) {
        return VARIANTS.getOrDefault(entity, DamageVariant.OTHER);
    }

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

    //? if <1.21.4 {
    /*private static boolean isSmashAttack(DamageSource source) {
        return source.getEntity() instanceof LivingEntity attacker
                && attacker.getWeaponItem().getItem() instanceof MaceItem
                && MaceItem.canSmashAttack(attacker);
    }
    *///?}
}
