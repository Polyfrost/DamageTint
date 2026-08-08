package org.polyfrost.damagetint.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polyfrost.compose.render.PolyColor;
import org.polyfrost.oneconfig.api.config.v1.serialize.adapter.impl.PolyColorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyColorMigrationTest {

    private static final int LEGACY_DEFAULT = 0x4CFF0000;

    private static final PolyColorAdapter ADAPTER = new PolyColorAdapter();

    @Test
    @DisplayName("an already-deserialized PolyColor passes straight through")
    public void acceptsAlreadyDeserializedColor() {
        PolyColor stored = new PolyColor(LEGACY_DEFAULT);

        PolyColor migrated = LegacyColorMigration.read(stored);

        Assertions.assertNotNull(migrated, "the stored colour must survive migration");
        Assertions.assertEquals(LEGACY_DEFAULT, migrated.getRawArgb());
    }

    @Test
    @DisplayName("the serialized int[] form still round-trips, alpha included")
    public void roundTripsSerializedForm() {
        PolyColor original = new PolyColor(LEGACY_DEFAULT);

        Object onDisk = ADAPTER.serialize(original);
        Assertions.assertArrayEquals(new int[]{255, 0, 0, 0x4C}, (int[]) onDisk,
                "serialize() writes r, g, b, a -- alpha is carried, not dropped");

        PolyColor migrated = LegacyColorMigration.read(onDisk);

        Assertions.assertNotNull(migrated);
        Assertions.assertEquals(original.getRawArgb(), migrated.getRawArgb());
        Assertions.assertEquals(0x4C, migrated.getAlpha());
        Assertions.assertEquals(255, migrated.getRed());
    }

    @Test
    @DisplayName("the JSON list form still round-trips")
    public void acceptsJsonListForm() {
        List<Object> onDisk = new ArrayList<>(Arrays.asList(255.0d, 0.0d, 0.0d, 76.0d));

        PolyColor migrated = LegacyColorMigration.read(onDisk);

        Assertions.assertNotNull(migrated);
        Assertions.assertEquals(LEGACY_DEFAULT, migrated.getRawArgb());
    }

    @Test
    @DisplayName("the chroma map form keeps its chroma settings")
    public void preservesChroma() {
        Map<String, Object> onDisk = new HashMap<>();
        onDisk.put("rgba", new int[]{255, 0, 0, 0x4C});
        onDisk.put("chroma", true);
        onDisk.put("chromaSpeed", 2.0d);

        PolyColor migrated = LegacyColorMigration.read(onDisk);

        Assertions.assertNotNull(migrated);
        Assertions.assertTrue(migrated.getChroma(), "chroma must survive migration");
        Assertions.assertEquals(2.0f, migrated.getChromaSpeed());
    }

    @Test
    @DisplayName("nothing saved means nothing to migrate")
    public void returnsNullForMissingValue() {
        Assertions.assertNull(LegacyColorMigration.read(null));
    }

    @Test
    @DisplayName("unreadable saved values fall back to the default instead of throwing")
    public void neverThrowsOnUnusableValues() {
        Object[] unusable = {
                "#4CFF0000",
                42,
                new int[]{255, 0},
                new ArrayList<>(Arrays.asList(1)),
                Collections.emptyMap(),
                new Object(),
        };

        for (Object value : unusable) {
            Assertions.assertNull(
                    Assertions.assertDoesNotThrow(
                            () -> LegacyColorMigration.read(value),
                            () -> "migration must not throw on " + value
                    ),
                    () -> "unreadable value should migrate to null: " + value
            );
        }
    }
}
