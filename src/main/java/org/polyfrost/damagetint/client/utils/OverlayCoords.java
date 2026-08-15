package org.polyfrost.damagetint.client.utils;

import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.DamageTintConfig;

public final class OverlayCoords {
    public static final int NO_OVERRIDE = Integer.MIN_VALUE;

    private static final int LAST_FADE_ROW = 7;

    public static int of(boolean hasRedOverlay, int hurtTime, int deathTime, DamageVariant variant, int vanillaU) {
        boolean separateColors = DamageTintConfig.DamageTypeColors.enabled;
        if (!DamageTintConfig.enabled || !hasRedOverlay || (!DamageTintConfig.fade && !separateColors)) {
            return NO_OVERRIDE;
        }

        int u = separateColors ? variant.column() : vanillaU;
        return OverlayTexture.pack(u, row(hurtTime, deathTime));
    }

    private static int row(int hurtTime, int deathTime) {
        if (!DamageTintConfig.fade) {
            return OverlayTexture.RED_OVERLAY_V;
        }

        if (deathTime > 0) {
            return DamageTintConfig.fadeDeath ? fadeRow((float) deathTime / DamageTintConfig.fadeDuration) : 0;
        }

        return fadeRow(1.0f - (float) hurtTime / DamageTintConfig.fadeDuration);
    }

    private static int fadeRow(float progress) {
        return Math.clamp(Math.round(progress * LAST_FADE_ROW), 0, LAST_FADE_ROW);
    }
}
