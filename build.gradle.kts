// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    dependencies {
        classpath("com.github.SoarY.GradleXFind:GradleFind:1.4.0")
//        classpath("com.soarsy.plugin:gradlexfind:1.4.0")
    }
}