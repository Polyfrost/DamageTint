package org.polyfrost.damagetint;

import net.fabricmc.api.ClientModInitializer;
import org.polyfrost.damagetint.client.DamageTintClient;

public class DamageTintEntrypoint implements ClientModInitializer {

    public void onInitializeClient() {
        DamageTintClient.INSTANCE.initialize();
    }
}
