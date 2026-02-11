import com.android.build.api.dsl.LibraryExtension
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.devtools.ksp)
}

configure<LibraryExtension> {
    namespace = "com.forknowledge.core.api"
    compileSdk = 36

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
}

kotlin {
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3
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
    ksp(libs.androidx.hilt.compiler)
}