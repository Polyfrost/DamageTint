package org.polyfrost.damagetint.client;

import org.polyfrost.oneconfig.api.commands.v1.CommandManager;

//#if MC >= 1.16.5
//$$ import org.polyfrost.oneconfig.api.event.v1.EventManager;
//$$ import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
//#endif

public class DamageTintClient {

    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        CommandManager.register(new DamageTintCommand());

        //#if MC >= 1.16.5
        //$$ EventManager.register(InitializationEvent.class, () -> DamageTintConfig.updateOverlayColor(DamageTintConfig.color));
        //#endif
    }

}
