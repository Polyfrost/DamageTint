package org.polyfrost.damagetint.config;

import org.polyfrost.damagetint.DamageTint;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Button;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.polyui.color.PolyColor;
import org.polyfrost.polyui.utils.ColorUtils;

public class DamageTintConfig extends Config {

    private static final int defaultColor = 1291780096;

    @Color(
            title = "Damage Tint Colour"
    )
    public static PolyColor color = ColorUtils.toColor(defaultColor);

    @Button(
            title = "Reset Damage Tint",
            text = "Reset Color"
    )
    Runnable resetColor = (() -> {
        color = ColorUtils.toColor(defaultColor);
        save();
        // openGui();
    });

    @Switch(
            title = "Fade Out Damage Tint"
    )
    public static boolean fade = false;

    /// private static final File oldModDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), "DamageTint");

    public DamageTintConfig() {
        super("damagetint.json", "/damagetint_dark.svg", DamageTint.NAME, Category.QOL); // new VigilanceMigrator(new File(oldModDir, "damagetint.toml").getPath()))
        save();
    }
}