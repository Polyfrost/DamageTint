package org.polyfrost.damagetint.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import org.polyfrost.damagetint.DamageTint;

@Command("damagetint")
public class DamageTintCommand {
    @Main
    public void handle() {
        DamageTint.config.openGui();
    }
}
