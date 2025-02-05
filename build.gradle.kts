import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "pro.horovodovodo4ka.dlssoverrides"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)

    implementation("com.mikepenz:multiplatform-markdown-renderer-jvm:0.30.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.30.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "DLSS Overrides enabler"
            packageVersion = "1.1.0"
            windows {
                shortcut = true
                menuGroup = "DLSS Overrides enabler"
                iconFile.set(project.file("src/main/resources/app-icon.ico"))
            }
        }
    }
}
