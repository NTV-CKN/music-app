// File: build.gradle.kts (Project)
plugins {
    alias(libs.plugins.android.application).apply(false) // androidApplication -> android.application
    alias(libs.plugins.kotlin.android).apply(false)      // kotlinAndroid -> kotlin.android
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.navigation.safeargs).apply(false) // navigationSafeargs -> navigation.safeargs
}