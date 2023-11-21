package org.polyfrost.damagetint;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.damagetint.command.DamageTintCommand;
import org.polyfrost.damagetint.config.DamageTintConfig;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static DamageTintConfig config;

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new DamageTintConfig();
        CommandManager.INSTANCE.registerCommand(new DamageTintCommand());
    }

}
