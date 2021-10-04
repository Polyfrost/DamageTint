package xyz.qalcyo.template.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import xyz.matthewtgm.requisite.util.GuiHelper;
import xyz.matthewtgm.requisite.util.Multithreading;
import xyz.matthewtgm.requisite.util.Notifications;
import xyz.qalcyo.template.ForgeTemplate;
import xyz.qalcyo.template.utils.Updater;

import java.io.File;

public class DownloadConfirmGui extends GuiScreen {
    private final GuiScreen parent;
    public DownloadConfirmGui(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 50, 200, 20, EnumChatFormatting.GREEN + "Yes"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 28, 200, 20, EnumChatFormatting.RED + "No"));
        super.initGui();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                GuiHelper.open(null);
                Multithreading.runAsync(() -> {
                    if (Updater.download(
                            Updater.updateUrl,
                            new File("mods/" + ForgeTemplate.NAME + "-" + StringUtils.substringAfter(Updater.latestTag, "v") + ".jar")
                    ) && Updater.download(
                            "https://github.com/Qalcyo/Deleter/releases/download/v1.2/Deleter-1.2.jar",
                            new File(ForgeTemplate.modDir.getParentFile(), "Deleter-1.2.jar")
                    )
                    ) {
                        Notifications
                                .push(ForgeTemplate.NAME, "The ingame updater has successfully installed the newest version.");
                        Updater.addShutdownHook();
                        Updater.shouldUpdate = false;
                    } else {
                        Notifications.push(
                                ForgeTemplate.NAME,
                                "The ingame updater has NOT installed the newest version as something went wrong."
                        );
                    }
                });
                break;
            case 2:
                GuiHelper.open(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2f, 2f, 0f);
        drawCenteredString(
                fontRendererObj,
                EnumChatFormatting.DARK_PURPLE + ForgeTemplate.NAME,
                width / 4,
                3,
                -1
        );
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1f, 1f, 0f);
        String[] lines = new String[]{
                "Are you sure you want to update?",
                "You can download it ingame at any time via the configuration screen.",
                "(This will update from v" + ForgeTemplate.VER + " to " + Updater.latestTag + ")"
        };
        int offset = Math.max(85 - lines.length * 10, 10);

        for (String line : lines) {
            drawCenteredString(mc.fontRendererObj, EnumChatFormatting.RED + line, width / 2, offset, -1);
            offset += mc.fontRendererObj.FONT_HEIGHT + 2;
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
