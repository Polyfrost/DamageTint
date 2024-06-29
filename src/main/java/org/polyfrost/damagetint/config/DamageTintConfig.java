package org.polyfrost.damagetint.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import net.minecraft.client.Minecraft;
import org.polyfrost.damagetint.DamageTint;

import java.io.File;

public class DamageTintConfig extends Config {

    @Exclude private static final int defaultColor = 1291780096;

    @Color(
            name = "Damage Tint Colour"
    )
    public static OneColor color = new OneColor(defaultColor);

    @Button(
            name = "Reset Damage Tint",
            text = "Reset Color"
    )
    Runnable resetColor = (() -> {
        color = new OneColor(defaultColor);
        save();
        openGui();
    });

    @Switch(
            name = "Fade Out Damage Tint"
    )
    public static boolean fade = false;

    @Exclude private static final File oldModDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), "DamageTint");

    public DamageTintConfig() {
        super(new Mod(DamageTint.NAME, ModType.UTIL_QOL, "/damagetint_dark.svg", new VigilanceMigrator(new File(oldModDir, "damagetint.toml").getPath())), "damagetint.json");
        initialize();
    }
}
