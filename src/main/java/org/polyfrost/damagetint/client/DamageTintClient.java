package org.polyfrost.damagetint.client;

import dev.deftu.omnicore.api.client.commands.OmniClientCommands;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

//#if MC >= 1.16.5
//$$ import org.polyfrost.oneconfig.api.event.v1.EventManager;
//$$ import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
//#endif

public class DamageTintClient {
    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        OmniClientCommands.register(OmniClientCommands.literal(DamageTintConstants.ID).executes(ctx -> {
            return ctx.getSource().openScreen(ScreensKt.createScreen(DamageTintConfig.INSTANCE));
        }));

        //#if MC >= 1.16.5
        //$$ EventManager.register(InitializationEvent.class, () -> DamageTintConfig.updateOverlayColor(DamageTintConfig.color));
        //#endif
    }
}
