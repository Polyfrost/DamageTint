package net.wyvest.damagetint.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import net.wyvest.damagetint.DamageTint;

@Command("damagetint")
public class DamageTintCommand {
    @Main
    public void handle() {
        DamageTint.config.openGui();
    }
}
