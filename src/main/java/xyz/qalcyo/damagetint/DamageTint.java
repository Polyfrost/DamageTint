package xyz.qalcyo.damagetint;

import gg.essential.api.EssentialAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.qalcyo.damagetint.command.DamageTintCommand;
import xyz.qalcyo.damagetint.config.Config;
import xyz.qalcyo.damagetint.utils.Updater;

import java.io.File;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static File modDir = new File(new File(new File(Minecraft.getMinecraft().mcDataDir, "config"), "Qalcyo"), NAME);
    public static void sendMessage(String message) {
        EssentialAPI.getMinecraftUtil().sendMessage(EnumChatFormatting.DARK_PURPLE + "[" + NAME + "] ", message);
    }
    public static Config config;
    public static DamageTintCommand command;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new Config();
        config.preload();
        command = new DamageTintCommand();
        command.register();
        Updater.update();
    }

}
