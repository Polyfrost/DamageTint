package org.polyfrost.damagetint.client.utils;

import net.minecraft.client.renderer.texture.OverlayTexture;

import java.util.ArrayDeque;

public final class ArmorOverlayContext {
    private static final Integer NO_ARMOR_OVERLAY = OverlayTexture.NO_OVERLAY;
    private static final ThreadLocal<ArrayDeque<Integer>> OVERLAYS = new ThreadLocal<>();

    private ArmorOverlayContext() {
    }

    public static void begin() {
        ArrayDeque<Integer> overlays = OVERLAYS.get();
        if (overlays == null) {
            overlays = new ArrayDeque<>();
            OVERLAYS.set(overlays);
        }
        overlays.push(NO_ARMOR_OVERLAY);
    }

    public static void capture(int overlayCoords, boolean tintArmor) {
        ArrayDeque<Integer> overlays = OVERLAYS.get();
        if (overlays == null || overlays.isEmpty()) {
            return;
        }

        overlays.pop();
        overlays.push(tintArmor ? overlayCoords : NO_ARMOR_OVERLAY);
    }

    public static int applyTo(int original) {
        ArrayDeque<Integer> overlays = OVERLAYS.get();
        return overlays == null || overlays.isEmpty() || overlays.peek() == OverlayTexture.NO_OVERLAY ? original : overlays.peek();
    }

    public static boolean isActive() {
        ArrayDeque<Integer> overlays = OVERLAYS.get();
        return overlays != null && !overlays.isEmpty() && overlays.peek() != OverlayTexture.NO_OVERLAY;
    }

    public static void end() {
        ArrayDeque<Integer> overlays = OVERLAYS.get();
        if (overlays == null) {
            return;
        }
        if (!overlays.isEmpty()) {
            overlays.pop();
        }
    }
}
