package xyz.qalcyo.template;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.matthewtgm.requisite.util.ChatHelper;
import xyz.qalcyo.template.utils.Updater;

import java.io.File;

@Mod(name = ForgeTemplate.NAME, version = ForgeTemplate.VER, modid = ForgeTemplate.ID)
public class ForgeTemplate {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static File modDir = new File(new File(new File(Minecraft.getMinecraft().mcDataDir, "config"), "Qalcyo"), NAME);
    public static void sendMessage(String message) {
        ChatHelper.sendMessage(EnumChatFormatting.DARK_PURPLE + "[" + NAME + "]", message);
    }

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        System.out.println("Hello.");
        Updater.update();
    }

}
