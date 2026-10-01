package org.polyfrost.damagetint.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.polyfrost.damagetint.client.utils.DamageVariantTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if =1.8.9 {
/*import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import org.spongepowered.asm.mixin.Unique;
*///?}

@Mixin(ClientPacketListener.class)
public class Mixin_ClientPacketListener {

    //? if =1.8.9 {
    /*@Unique
    private static final int DAMAGE_TINT$CRIT_ANIMATION = 4;

    @Unique
    private static final int DAMAGE_TINT$HURT_EVENT = 2;

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void damageTint$recordHurt(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(Minecraft.getInstance().level);
        if (packet.getEvent() == DAMAGE_TINT$HURT_EVENT && entity instanceof LivingEntity living) {
            DamageVariantTracker.record(living);
        }
    }
    *///?}

    @Inject(method = "handleAnimate", at = @At("TAIL"))
    private void damageTint$recordCrit(ClientboundAnimatePacket packet, CallbackInfo ci) {
        //~ if =1.8.9 'ClientboundAnimatePacket.CRITICAL_HIT' -> 'DAMAGE_TINT$CRIT_ANIMATION'
        if (packet.getAction() != ClientboundAnimatePacket.CRITICAL_HIT) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        Entity entity = minecraft.level.getEntity(packet.getId());
        if (entity instanceof LivingEntity living) {
            DamageVariantTracker.recordCrit(living);
        }
    }
}
