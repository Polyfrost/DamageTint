package org.polyfrost.damagetint.client;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageTintClient {

    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public final Logger LOGGER = LoggerFactory.getLogger("DamageTint");

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        EventManager.register(InitializationEvent.class, () -> DamageTintConfig.updateOverlayColor(DamageTintConfig.color));
    }
}
