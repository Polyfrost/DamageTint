package org.polyfrost.damagetint.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.damagetint.client.utils.DamageVariant;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.ConfigManager;
import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.annotations.Accordion;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Include;
import org.polyfrost.oneconfig.api.config.v1.annotations.Info;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.oneconfig.api.config.v1.serialize.adapter.impl.PolyColorAdapter;

import java.util.Arrays;

public class DamageTintConfig extends Config {

    @Switch(title = "Enable Damage Tint")
    public static boolean enabled = true;

    private static final int defaultColor = 0x4DFF0000;
    private static final int legacyDefaultColor = 1291780096;

    @Color(title = "Damage Tint Color")
    public static PolyColor colorV2 = new PolyColor(defaultColor);

    @Accordion(title = "Separate Colors Per Damage Type")
    public static class DamageTypeColors {

        @Include
        public static boolean enabled = false;

        @Info(title = "Colors may be inaccurate on some servers",
                description = "Some servers do not report the damage type correctly, so hits may be tinted with the wrong color or fall back to the main damage tint color.")
        public static String warning = "";

        @Color(title = "Melee Damage Color")
        public static PolyColor melee = new PolyColor(defaultColor);

        @Color(title = "Mace Damage Color")
        public static PolyColor mace = new PolyColor(defaultColor);

        @Color(title = "Ranged Damage Color")
        public static PolyColor ranged = new PolyColor(defaultColor);

        @Color(title = "Explosion Damage Color")
        public static PolyColor explosion = new PolyColor(defaultColor);

        @Color(title = "Magic Damage Color")
        public static PolyColor magic = new PolyColor(defaultColor);

        @Color(title = "Critical Hit Color")
        public static PolyColor crit = new PolyColor(defaultColor);
    }

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    @Slider(title = "Fade Duration (ticks)", min = 1, max = 10, step = 1)
    public static float fadeDuration = 10;

    @Switch(title = "Fade Out Dead Entities")
    public static boolean fadeDeath = false;

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    public DamageTintConfig() {
        super("damagetint.json", "/assets/damagetint/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);

        Tree oldConfig = ConfigManager.active().load(id);
        Object oldRaw = oldConfig == null || oldConfig.getProp("color") == null ? null : oldConfig.getProp("color").get();
        PolyColor oldColor = oldRaw == null ? null
                : oldRaw instanceof PolyColor ? (PolyColor) oldRaw
                : new PolyColorAdapter().deserialize(oldRaw);

        for (String option : new String[]{"enabled", "colorV2", "fade",
                "DamageTypeColors.enabled", "DamageTypeColors.melee", "DamageTypeColors.mace",
                "DamageTypeColors.ranged", "DamageTypeColors.explosion", "DamageTypeColors.magic",
                "DamageTypeColors.crit"}) {
            addCallback(option, DamageTintConfig::updateOverlayColors);
        }

        if (oldColor != null && (oldColor.getChroma() || oldColor.getRawArgb() != legacyDefaultColor)) {
            colorV2 = oldColor;
        }
        save();
    }

    public static boolean hasChroma() {
        if (colorV2.getChroma()) {
            return true;
        }

        return DamageTypeColors.enabled
                && (DamageTypeColors.melee.getChroma() || DamageTypeColors.mace.getChroma()
                || DamageTypeColors.ranged.getChroma() || DamageTypeColors.explosion.getChroma()
                || DamageTypeColors.magic.getChroma() || DamageTypeColors.crit.getChroma());
    }

    public static void updateOverlayColors() {
        Minecraft.getInstance().execute(() -> {
            int[] columns = new int[DamageVariant.COLUMNS];
            Arrays.fill(columns, enabled ? colorV2.getArgb() : defaultColor);

            if (enabled && DamageTypeColors.enabled) {
                columns[DamageVariant.MELEE.column()] = DamageTypeColors.melee.getArgb();
                columns[DamageVariant.MACE.column()] = DamageTypeColors.mace.getArgb();
                columns[DamageVariant.RANGED.column()] = DamageTypeColors.ranged.getArgb();
                columns[DamageVariant.EXPLOSION.column()] = DamageTypeColors.explosion.getArgb();
                columns[DamageVariant.MAGIC.column()] = DamageTypeColors.magic.getArgb();
                columns[DamageVariant.CRIT.column()] = DamageTypeColors.crit.getArgb();
            }

            OverlayTexture overlayTexture = Minecraft.getInstance().gameRenderer.overlayTexture();
            ((OverlayModifier) overlayTexture).damageTint$setOverlayColors(columns, enabled && fade);
        });
    }
}
