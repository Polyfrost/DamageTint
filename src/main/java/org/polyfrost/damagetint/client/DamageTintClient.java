package org.polyfrost.damagetint.client;

import org.polyfrost.oneconfig.api.commands.v1.CommandManager;

public class DamageTintClient {

    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        CommandManager.registerCommand(new DamageTintCommand());
    }

}
