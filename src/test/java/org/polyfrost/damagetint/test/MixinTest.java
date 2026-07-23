package org.polyfrost.damagetint.test;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

/**
 * Audits mixins to ensure their validity without launching a full Minecraft client.
 * Implementation inspired by <a href="https://github.com/SkyblockerMod/Skyblocker">Skyblocker</a>.
 */
public class MixinTest {

    @BeforeAll
    public static void setupEnvironment() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    @DisplayName("mixins load successfully")
    public void auditMixins() {
        MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        Assertions.assertInstanceOf(
                IMixinTransformer.class,
                environment.getActiveTransformer()
        );
        environment.audit();
    }
}
