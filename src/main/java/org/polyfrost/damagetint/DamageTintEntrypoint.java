package org.polyfrost.damagetint;

//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer;
//$$ import net.fabricmc.api.ClientModInitializer;
//$$ import net.fabricmc.api.DedicatedServerModInitializer;
//#elseif FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.eventbus.api.IEventBus;
//$$ import net.minecraftforge.fml.common.Mod;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//#else
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//#endif
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus;
//$$ import net.neoforged.fml.common.Mod;
//$$ import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
//$$ import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//$$ import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
//#endif

import org.polyfrost.damagetint.client.DamageTintClient;

//#if FORGE-LIKE
//$$ import org.polyfrost.damagetint.DamageTintConstants;
//$$
//#if MC >= 1.16.5
//$$ @Mod(DamageTintConstants.ID)
//#else
@Mod(modid = DamageTintConstants.ID, version = DamageTintConstants.VERSION)
//#endif
//#endif
public class DamageTintEntrypoint
    //#if FABRIC
    //$$ implements ClientModInitializer
    //#endif
{

    //#if FORGE && MC >= 1.16.5
    //$$ public DamageTintEntrypoint() {
    //$$     setupForgeEvents(FMLJavaModLoadingContext.get().getModEventBus());
    //$$ }
    //#elseif NEOFORGE
    //$$ public DamageTintEntrypoint(IEventBus modEventBus) {
    //$$     setupForgeEvents(modEventBus);
    //$$ }
    //#endif

    //#if FABRIC
    //$$ @Override
    //#elseif FORGE && MC <= 1.12.2
    @EventHandler
    //#endif
    public void onInitializeClient(
            //#if FORGE-LIKE
            //#if MC >= 1.16.5
            //$$ FMLClientSetupEvent event
            //#else
            FMLInitializationEvent event
            //#endif
            //#endif
    ) {
        //#if FORGE && MC <= 1.12.2
        //$$ if (!event
        //#if MC <= 1.8.9
        //$$ .side.isClient
        //#else
        //$$ .getSide().isClient()
        //#endif
        //$$ ) {
        //$$     return;
        //$$ }
        //#endif

        DamageTintClient.INSTANCE.initialize();
    }

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private void setupForgeEvents(IEventBus modEventBus) {
    //$$     modEventBus.addListener(this::onInitializeClient);
    //$$ }
    //#endif

}
