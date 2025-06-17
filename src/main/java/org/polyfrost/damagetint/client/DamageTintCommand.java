package org.polyfrost.damagetint.client;

import org.polyfrost.damagetint.DamageTintConstants;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
 * @see DamageTintClient
 */
@Command(DamageTintConstants.ID)
public class DamageTintCommand {

    @Handler
    private void main() {
        ScreensKt.openUI(DamageTintConfig.INSTANCE);
    }

}
