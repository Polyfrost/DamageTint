package org.polyfrost.damagetint.client;

//? if >1.8.9 {
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.polyfrost.damagetint.client.utils.OverlayModifier;
//?}
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.FramebufferRenderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageTintClient {

    private static final long CHROMA_TIME_NANOS = 1_000_000_000L / 60;

    public static final DamageTintClient INSTANCE = new DamageTintClient();

    public final Logger LOGGER = LoggerFactory.getLogger("DamageTint");
    private long lastChromaTime;
    //? if >1.8.9 {
    private OverlayTexture tintedOverlay;
    //?}

    public void initialize() {
        DamageTintConfig.INSTANCE.preload();
        EventManager.register(FramebufferRenderEvent.Start.class, this::onFrame);
    }

    private void onFrame() {
        //? if >1.8.9 {
        // Checked per frame, not at construction, so a mod that builds and customizes its own overlay is tinted after its edits
        OverlayTexture overlay = Minecraft.getInstance().gameRenderer.overlayTexture();
        if (overlay != tintedOverlay) {
            tintedOverlay = overlay;
            DamageTintConfig.applyOverlayColors((OverlayModifier) overlay);
        }
        //?}
        updateChroma();
    }

    private void updateChroma() {
        if (!DamageTintConfig.enabled || !DamageTintConfig.hasChroma()) {
            lastChromaTime = 0L;
            return;
        }

        long now = System.nanoTime();
        if (now - lastChromaTime < CHROMA_TIME_NANOS) {
            return;
        }

        lastChromaTime = now;
        DamageTintConfig.updateOverlayColors();
    }
}
