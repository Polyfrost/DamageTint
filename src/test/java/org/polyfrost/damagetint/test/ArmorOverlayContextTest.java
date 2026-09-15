package org.polyfrost.damagetint.test;

import net.minecraft.client.renderer.texture.OverlayTexture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.polyfrost.damagetint.client.utils.ArmorOverlayContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArmorOverlayContextTest {

    @AfterEach
    void clearContext() {
        ArmorOverlayContext.end();
        ArmorOverlayContext.end();
    }

    @Test
    void appliesCapturedOverlayOnlyWhenEnabled() {
        assertEquals(4321, ArmorOverlayContext.applyTo(4321));

        ArmorOverlayContext.begin();
        ArmorOverlayContext.capture(1234, false);
        assertFalse(ArmorOverlayContext.isActive());
        assertEquals(4321, ArmorOverlayContext.applyTo(4321));

        ArmorOverlayContext.capture(1234, true);
        assertTrue(ArmorOverlayContext.isActive());
        assertEquals(1234, ArmorOverlayContext.applyTo(OverlayTexture.NO_OVERLAY));
    }

    @Test
    void restoresOuterOverlayAfterNestedRender() {
        ArmorOverlayContext.begin();
        ArmorOverlayContext.capture(111, true);

        ArmorOverlayContext.begin();
        ArmorOverlayContext.capture(222, true);
        assertEquals(222, ArmorOverlayContext.applyTo(OverlayTexture.NO_OVERLAY));

        ArmorOverlayContext.end();
        assertEquals(111, ArmorOverlayContext.applyTo(OverlayTexture.NO_OVERLAY));

        ArmorOverlayContext.end();
        assertFalse(ArmorOverlayContext.isActive());
    }

    @Test
    void inactiveNestedRenderMasksOuterOverlay() {
        ArmorOverlayContext.begin();
        ArmorOverlayContext.capture(111, true);

        ArmorOverlayContext.begin();
        assertFalse(ArmorOverlayContext.isActive());
        assertEquals(4321, ArmorOverlayContext.applyTo(4321));

        ArmorOverlayContext.end();
        assertTrue(ArmorOverlayContext.isActive());
        assertEquals(111, ArmorOverlayContext.applyTo(4321));
    }
}
