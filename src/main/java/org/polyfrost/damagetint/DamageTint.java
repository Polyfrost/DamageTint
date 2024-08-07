package org.polyfrost.damagetint;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.damagetint.command.DamageTintCommand;
import org.polyfrost.damagetint.config.DamageTintConfig;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static DamageTintConfig config;

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new DamageTintConfig();
        CommandManager.registerCommand(new DamageTintCommand());
    }
}
