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

@Mixin(ClientPacketListener.class)
public class Mixin_ClientPacketListener {

    @Inject(method = "handleAnimate", at = @At("TAIL"))
    private void damageTint$recordCrit(ClientboundAnimatePacket packet, CallbackInfo ci) {
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
