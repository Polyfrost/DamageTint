package org.polyfrost.damagetint.client;

import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.OmniClientRuntime;
import dev.deftu.omnicore.api.color.OmniColor;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Button;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.polyui.color.ColorUtils;
import org.polyfrost.polyui.color.PolyColor;

public class DamageTintConfig extends Config {

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    @Switch(title = "Enable Damage Tint")
    public static boolean enabled = true;

    private static final int defaultColor = 1291780096;

    @Color(title = "Damage Tint Color")
    public static PolyColor color = ColorUtils.toColor(defaultColor);

    @Button(title = "Reset Damage Tint", text = "Reset Color")
    private void resetColor() {
        color = ColorUtils.asMutable(ColorUtils.toColor(defaultColor));
        save();
    }

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    public DamageTintConfig() {
        super("damagetint.json", "/assets/damagetint/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);
        save();
    }

    public static void updateOverlayColor(PolyColor newColor) {
        OmniClientRuntime.runOnMain(() -> {
            int r = newColor.red();
            int g = newColor.green();
            int b = newColor.blue();
            // Alpha is flipped for some reason, so 0 is fully opaque and 255 is fully transparent... Why, Mojang? Other developer note: 😭😭😭😭😭
            int a = 255 - newColor.alpha();

            OmniColor color = new OmniColor(r, g, b, a);
            DamageTintClient.INSTANCE.LOGGER.info("updateOverlayColor called");

            OverlayTexture overlayTexture = OmniClient.get().gameRenderer.overlayTexture();
            ((OverlayModifier)overlayTexture).damageTint$setOverlayColor(color);
        });
    }
}
