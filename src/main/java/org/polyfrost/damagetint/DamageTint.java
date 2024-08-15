package org.polyfrost.damagetint;

import org.polyfrost.damagetint.command.DamageTintCommand;
import org.polyfrost.damagetint.config.DamageTintConfig;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;

public class DamageTint {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static DamageTintConfig config;

    public static void initialize() {
        config = new DamageTintConfig();
        CommandManager.registerCommand(new DamageTintCommand());
    }
}
