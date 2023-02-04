package net.wyvest.damagetint.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import net.wyvest.damagetint.DamageTint;

import java.io.File;

public class DamageTintConfig extends Config {

    @Color(
            name = "Damage Tint Colour"
    )
    public static OneColor color = new OneColor(255, 0, 0, 76);

    @Switch(
            name = "Fade Out Damage Tint"
    )
    public static boolean fade = false;

    public DamageTintConfig() {
        super(new Mod(DamageTint.NAME, ModType.UTIL_QOL, "/damagetint_dark.svg", new VigilanceMigrator(new File(DamageTint.modDir, "damagetint.toml").getPath())), "damagetint.json");
    }
}
