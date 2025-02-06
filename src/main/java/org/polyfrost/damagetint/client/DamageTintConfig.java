package org.polyfrost.damagetint.client;

import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Button;
import org.polyfrost.oneconfig.api.config.v1.annotations.Color;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.polyui.color.ColorUtils;
import org.polyfrost.polyui.color.PolyColor;

//#if MC >= 1.16.5
//$$ import net.minecraft.client.MinecraftClient;
//$$ import net.minecraft.client.render.OverlayTexture;
//$$ import org.polyfrost.damagetint.client.utils.OverlayModifier;
//#endif

public class DamageTintConfig extends Config {

    public static final DamageTintConfig INSTANCE = new DamageTintConfig();

    private static final int defaultColor = java.awt.Color.GREEN.getRGB(); // 1291780096;

    @Color(title = "Damage Tint Colour")
    public static PolyColor color = ColorUtils.toColor(defaultColor);

    @Button(title = "Reset Damage Tint", text = "Reset Color")
    private void resetColor() {
        color = ColorUtils.mutable(ColorUtils.toColor(defaultColor));
        save();
    }

    //#if MC >= 1.16.5
    //$$ @Button(title = "Fix Damage Tint Colour", text = "Fix Color")
    //$$ private void fixColor() {
    //$$     updateOverlayColor(color);
    //$$ }
    //#endif

    @Switch(title = "Fade Out Damage Tint")
    public static boolean fade = false;

    public DamageTintConfig() {
        super("damagetint.json", "/damagetint_dark.svg", DamageTintConstants.NAME, Category.QOL);
        save();

        //#if MC >= 1.16.5
        //$$ addCallback("color", () -> {
        //$$     System.out.println("addCallback(color): " + color);
        //$$     updateOverlayColor(color);
        //$$ });
        //#endif
    }

    //#if MC >= 1.16.5
    //$$ public static void updateOverlayColor(PolyColor newColor) {
    //$$     MinecraftClient.getInstance().execute(() -> {
    //$$         int red = newColor.red();
    //$$         int green = newColor.green();
    //$$         int blue = newColor.blue();
    //$$         // Alpha is flipped for some reason, so 0 is fully opaque and 255 is fully transparent... Why, Mojang?
    //$$         int alpha = 255 - newColor.alpha();
    //$$         int abgr = alpha << 24 | blue << 16 | green << 8 | red;
    //$$
    //$$         OverlayTexture overlayTexture = MinecraftClient.getInstance().gameRenderer.getOverlayTexture();
    //$$         ((OverlayModifier) overlayTexture).setOverlayColor(abgr);
    //$$     });
    //$$ }
    //#endif

}
