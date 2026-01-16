package org.polyfrost.damagetint.client;

import dev.deftu.omnicore.api.client.commands.OmniClientCommands;
import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DamageTintClient {
    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public final Logger LOGGER = LoggerFactory.getLogger("DamageTint");

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        OmniClientCommands.register(OmniClientCommands.literal(DamageTintConstants.ID).executes(ctx -> ctx.getSource().openScreen(ScreensKt.createScreen(DamageTintConfig.INSTANCE))));
        EventManager.register(InitializationEvent.class, () -> DamageTintConfig.updateOverlayColor(DamageTintConfig.color));
    }
}
