import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.forknowledge.core.api"
    compileSdk = 35

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())

        buildConfigField("String", "API_URL", properties.getProperty("API_URL"))
        buildConfigField("String", "API_IMAGE_URL", properties.getProperty("API_IMAGE_URL"))
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.com.squareup.retrofit2)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.com.squareup.okhttp3.okhttp)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    implementation(libs.androidx.hilt)
    kapt(libs.androidx.hilt.compiler)
}