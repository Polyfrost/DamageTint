package org.polyfrost.damagetint;

//#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(name = DamageTint.NAME, version = DamageTint.VER, modid = DamageTint.ID)
public class DamageTintForge {
    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        DamageTint.initialize();
    }
}
//#endif