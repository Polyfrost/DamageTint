package org.polyfrost.damagetint.client;

import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
 * @see DamageTintClient
 */
@Command(value = DamageTintConstants.ID, description = "Access the " + DamageTintConstants.NAME + " GUI.")
public class DamageTintCommand {

    @Command
    private void main() {
        ScreensKt.openUI(DamageTintConfig.INSTANCE);
    }

}
