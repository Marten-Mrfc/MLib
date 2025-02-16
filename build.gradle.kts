import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    java
    idea
    `maven-publish`
}

val projectGroup = "mlib.api"
val projectVersion = "0.8.1"

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://libraries.minecraft.net")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(kotlin("reflect"))
    compileOnly("net.kyori:adventure-text-minimessage:4.13.1")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.mojang:brigadier:1.0.18")
    compileOnly("me.clip:placeholderapi:2.11.6")
    api("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.21.0")
}

java {
    withSourcesJar()

    val javaVersion = JavaVersion.toVersion(21)
    sourceCompatibility = javaVersion
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "21"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

publishing {
    publications {
        create<MavenPublication>("main") {
            group = projectGroup
            version = projectVersion
            artifactId = "MLib"

            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}