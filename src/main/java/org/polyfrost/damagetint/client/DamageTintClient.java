package org.polyfrost.damagetint.client;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.FramebufferRenderEvent;
import org.polyfrost.oneconfig.api.event.v1.events.InitializationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageTintClient {

    private static final long CHROMA_TIME_NANOS = 1_000_000_000L / 60;

    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public final Logger LOGGER = LoggerFactory.getLogger("DamageTint");
    private long lastChromaTime;

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        EventManager.register(InitializationEvent.class, () -> DamageTintConfig.updateOverlayColor(DamageTintConfig.color));
        EventManager.register(FramebufferRenderEvent.Start.class, this::updateChroma);
    }

    private void updateChroma() {
        if (!DamageTintConfig.enabled || !DamageTintConfig.color.getChroma()) {
            lastChromaTime = 0L;
            return;
        }

        long now = System.nanoTime();
        if (now - lastChromaTime < CHROMA_TIME_NANOS) {
            return;
        }

        lastChromaTime = now;
        DamageTintConfig.updateOverlayColor(DamageTintConfig.color);
    }
}
