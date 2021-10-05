package xyz.qalcyo.damagetint.command;

import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import xyz.qalcyo.damagetint.DamageTint;

public class DamageTintCommand extends Command {
    public DamageTintCommand() {
        super("damagetint", true);
    }

    @DefaultHandler
    public void handle() {
        EssentialAPI.getGuiUtil().openScreen(DamageTint.config.gui());
    }
}
