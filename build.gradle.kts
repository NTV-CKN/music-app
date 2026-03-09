// File: build.gradle.kts (Project)
plugins {
    alias(libs.plugins.android.application).apply(false) // androidApplication -> android.application
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.navigation.safeargs).apply(false)
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}