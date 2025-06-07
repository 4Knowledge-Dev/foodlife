import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.forknowledge.core.domain"
    compileSdk = 35

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())
        buildConfigField("String", "GOOGLE_CLIENT_ID", properties.getProperty("GOOGLE_CLIENT_ID"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))

    implementation(libs.androidx.hilt)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.credentials.manager)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.identity.googleid)
    implementation(libs.play.services)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.facebook.login)
}
