// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        // Mappls services Gradle plugin
        maven {
            url = uri("https://maven.mappls.com/repository/mappls/")
        }
    }
    dependencies {
        // Mappls services Gradle plugin (handles reading .a.conf / .a.olf)
        classpath("com.mappls.services:mappls-services:1.0.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
