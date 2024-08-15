plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("org.polyfrost.multi-version.root")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

preprocess {
    strictExtraMappings.set(true)
    val fabric10809 = createNode("1.8.9-fabric", 10809, "yarn")

    val forge10809 = createNode("1.8.9-forge", 10809, "srg")

    fabric10809.link(forge10809)
}