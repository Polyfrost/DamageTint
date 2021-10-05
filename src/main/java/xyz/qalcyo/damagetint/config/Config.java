package xyz.qalcyo.damagetint.config;

import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;
import net.minecraft.client.Minecraft;
import xyz.qalcyo.damagetint.DamageTint;
import xyz.qalcyo.damagetint.gui.DownloadConfirmGui;
import xyz.qalcyo.damagetint.utils.Updater;

import java.awt.*;
import java.io.File;

public class Config extends Vigilant {
    @Property(
            type = PropertyType.SWITCH,
            name = "Toggle Mod",
            description = "Toggle the mod's functionality.",
            category = "General"
    )
    public static boolean toggle = true;

    @Property(
            type = PropertyType.COLOR,
            name = "Damage Tint Colour",
            description = "Modify the damage tint when mobs take damage.",
            category = "General",
            allowAlpha = false
    )
    public static Color color = Color.RED;

    @Property(
            type = PropertyType.SWITCH,
            name = "Show Update Notification",
            description = "Show a notification when you start Minecraft informing you of new updates.",
            category = "Updater"
    )
    public static boolean showUpdate = true;

    @Property(
            type = PropertyType.BUTTON,
            name = "Update Now",
            description = "Update by clicking the button.",
            category = "Updater"
    )
    public void update() {
        if (Updater.shouldUpdate) EssentialAPI.getGuiUtil()
                .openScreen(new DownloadConfirmGui(Minecraft.getMinecraft().currentScreen)); else EssentialAPI.getNotifications()
                .push("Damage Tint", "No update had been detected at startup, and thus the update GUI has not been shown.");
    }

    public Config() {
        super(new File(DamageTint.modDir, "damagetint.toml"), "Damage Tint");
    }
}
