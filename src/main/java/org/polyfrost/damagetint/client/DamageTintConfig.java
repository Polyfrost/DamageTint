package org.polyfrost.damagetint.client;

//? if > 1.8.9 {
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
//?}
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.ConfigManager;
import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.oneconfig.api.config.v1.serialize.adapter.impl.PolyColorAdapter;

public class DamageTintConfig extends Config {

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

        Tree oldConfig = ConfigManager.active().load(id);
        // OneConfig already runs the type adapter while loading, so the property normally holds a
        // PolyColor. Older configs stored the raw array form, which still needs the adapter.
        Object oldRaw = oldConfig == null || oldConfig.getProp("color") == null ? null : oldConfig.getProp("color").get();
        PolyColor oldColor = oldRaw == null ? null
                : oldRaw instanceof PolyColor ? (PolyColor) oldRaw
                : new PolyColorAdapter().deserialize(oldRaw);

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

    public static void updateOverlayColor(PolyColor newColor) {
       //? if > 1.8.9 {
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
        //?}
    }
}
