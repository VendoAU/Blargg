plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
}

group = "com.vendoau"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Minestom:Minestom:aebf72de90")

    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
    implementation("com.github.mworzala.mc_debug_renderer:minestom:2c354a8e08")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    jar {
        manifest {
            attributes["Main-Class"] = "com.vendoau.blargg.Main"
        }
    }
}