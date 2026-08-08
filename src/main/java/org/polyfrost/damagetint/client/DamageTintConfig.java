package org.polyfrost.damagetint.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.ConfigManager;
import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageTintConfig extends Config {

    private static final Logger LOGGER = LoggerFactory.getLogger("DamageTint/Config");

    @Switch(title = "Enable Damage Tint")
    public static boolean enabled = true;

    private static final int defaultColor = 0x4DFF0000;
    private static final int legacyDefaultColor = 1291780096;

    @Color(title = "Damage Tint Color")
    public static PolyColor colorV2 = new PolyColor(defaultColor);

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    @Slider(title = "Fade Duration (ticks)", min = 1, max = 10, step = 1)
    public static float fadeDuration = 10;

    @Switch(title = "Fade Out Dead Entities")
    public static boolean fadeDeath = false;

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    public DamageTintConfig() {
        super("damagetint.json", "/assets/damagetint/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);

        PolyColor oldColor = readLegacyColor();

        addCallback("enabled", () -> updateOverlayColor(colorV2));
        addCallback("colorV2", (() -> {
            updateOverlayColor(colorV2);
        }));
        addCallback("fade", () -> updateOverlayColor(colorV2));
        if (oldColor != null && (oldColor.getChroma() || oldColor.getRawArgb() != legacyDefaultColor)) {
            colorV2 = oldColor;
        }
        save();
    }

    private PolyColor readLegacyColor() {
        try {
            Tree oldConfig = ConfigManager.active().load(id);
            if (oldConfig == null || oldConfig.getProp("color") == null) {
                return null;
            }
            return LegacyColorMigration.read(oldConfig.getProp("color").get());
        } catch (Throwable t) {
            LOGGER.warn("Could not load the existing {} config to migrate its colour; "
                    + "continuing with the default colour.", id, t);
            return null;
        }
    }

    public static void updateOverlayColor(PolyColor newColor) {
       Minecraft.getInstance().execute(() -> {
            int argb = enabled ? newColor.getArgb() : defaultColor;
            int r = argb >> 16 & 0xFF;
            int g = argb >> 8 & 0xFF;
            int b = argb & 0xFF;
            // Alpha is flipped for some reason, so 0 is fully opaque and 255 is fully transparent... Why, Mojang? Other developer note: 😭😭😭😭😭
            int a = 255 - (argb >>> 24);

            OverlayTexture overlayTexture = Minecraft.getInstance().gameRenderer.overlayTexture();
            ((OverlayModifier)overlayTexture).damageTint$setOverlayColor(a, r, g, b, enabled && fade);
        });
    }
}
