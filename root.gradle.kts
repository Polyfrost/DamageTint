plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("org.polyfrost.multi-version.root")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

preprocess {
    strictExtraMappings.set(true)
    val fabric10809 = createNode("1.8.9-fabric", 10809, "yarn")
    val fabric11202 = createNode("1.12.2-fabric", 11202, "srg")

    val forge10809 = createNode("1.8.9-forge", 10809, "srg")
    val forge11202 = createNode("1.12.2-forge", 11202, "srg")

    fabric10809.link(forge10809)
    fabric11202.link(fabric10809)

    forge11202.link(fabric11202)
}