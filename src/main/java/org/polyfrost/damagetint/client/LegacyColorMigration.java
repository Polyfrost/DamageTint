package org.polyfrost.damagetint.client;

import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.serialize.adapter.impl.PolyColorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LegacyColorMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger("DamageTint/ConfigMigration");

    private LegacyColorMigration() {
    }

    static PolyColor read(Object raw) {
        if (raw == null) {
            return null;
        }

        // OneConfig already runs the type adapter while loading, so the property normally holds a
        // PolyColor. Older configs stored the raw array form, which still needs the adapter.
        if (raw instanceof PolyColor) {
            return (PolyColor) raw;
        }

        try {
            return new PolyColorAdapter().deserialize(raw);
        } catch (Throwable t) {
            LOGGER.warn("Could not read the damage tint colour saved by an older version ({}); "
                    + "falling back to the default colour.", raw, t);
            return null;
        }
    }
}
