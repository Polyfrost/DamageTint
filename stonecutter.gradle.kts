plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.2" /* [SC] DO NOT EDIT */
stonecutter {
    parameters {
        replacements {
            string(eval(current.version, "= 1.8.9")) {
                replace(
                    "net.minecraft.server.Bootstrap",
                    "net.minecraft.Bootstrap"
                )
            }
        }
    }
}
