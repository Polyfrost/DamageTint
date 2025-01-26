package org.polyfrost.damagetint.client;

import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Button;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.polyui.color.ColorUtils;
import org.polyfrost.polyui.color.PolyColor;

public class DamageTintConfig extends Config {

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    private static final int defaultColor = 1291780096;

    @Color(title = "Damage Tint Colour")
    public static PolyColor color = ColorUtils.toColor(defaultColor);

    @Button(title = "Reset Damage Tint", text = "Reset Color")
    private void resetColor() {
        color = ColorUtils.toColor(defaultColor);
        save();
    }

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    public DamageTintConfig() {
        super("damagetint.json", "/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);
        save();
    }

}
