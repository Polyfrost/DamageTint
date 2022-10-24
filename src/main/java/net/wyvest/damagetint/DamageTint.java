package net.wyvest.damagetint;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.wyvest.damagetint.command.DamageTintCommand;
import net.wyvest.damagetint.config.DamageTintConfig;

import java.io.File;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);
    public static DamageTintConfig config;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists() && !modDir.mkdirs()) {
            throw new RuntimeException("Failed to create mod directory! Please report this: https://polyfrost.cc/discord");
        }
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new DamageTintConfig();
        config.initialize();
        CommandManager.INSTANCE.registerCommand(new DamageTintCommand());
    }

}
