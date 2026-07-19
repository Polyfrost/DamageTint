package org.polyfrost.damagetint.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public class DamageTintConfig extends Config {

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    @Switch(title = "Enable Damage Tint")
    public static boolean enabled = true;

    private static final int defaultColor = 1291780096;

    @Color(title = "Damage Tint Color")
    public static PolyColor color = new PolyColor(defaultColor);

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    @Slider(title = "Fade Duration (ticks)", min = 1, max = 10, step = 1)
    public static float fadeDuration = 10;

    @Switch(title = "Fade Out Dead Entities")
    public static boolean fadeDeath = false;

    public DamageTintConfig() {
        super("damagetint.json", "/assets/damagetint/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);
        addCallback("color", (() -> {
            updateOverlayColor(color);
        }));
        save();
    }

    public static void updateOverlayColor(PolyColor newColor) {
       Minecraft.getInstance().execute(() -> {
            int r = newColor.getRed();
            int g = newColor.getGreen();
            int b = newColor.getBlue();
            // Alpha is flipped for some reason, so 0 is fully opaque and 255 is fully transparent... Why, Mojang? Other developer note: 😭😭😭😭😭
            int a = 255 - newColor.getAlpha();

            OverlayTexture overlayTexture = Minecraft.getInstance().gameRenderer.overlayTexture();
            ((OverlayModifier)overlayTexture).damageTint$setOverlayColor(a, r, g, b);
        });
    }
}
