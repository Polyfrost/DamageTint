package org.polyfrost.damagetint.client.utils;

public enum DamageVariant {
    OTHER(0),
    MELEE(1),
    RANGED(2),
    MAGIC(3),
    CRIT(4),
    MACE(5),
    EXPLOSION(6);

    public static final int COLUMNS = 16;

    private final int column;

    DamageVariant(int column) {
        this.column = column;
    }

    public int column() {
        return column;
    }
}
