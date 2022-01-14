package net.wyvest.damagetint;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.wyvest.damagetint.command.DamageTintCommand;
import net.wyvest.damagetint.config.Config;
import net.wyvest.damagetint.updater.Updater;

import java.io.File;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);
    public static Config config;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new Config();
        config.preload();
        new DamageTintCommand().register();
        Updater.update();
    }

}
