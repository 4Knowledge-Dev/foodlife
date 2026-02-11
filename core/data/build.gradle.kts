import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.devtools.ksp)
}

configure<LibraryExtension> {
    namespace = "com.forknowledge.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())
        buildConfigField("String", "GOOGLE_CLIENT_ID", properties.getProperty("GOOGLE_CLIENT_ID"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:api"))

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.com.squareup.retrofit2)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.credentials.manager)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.identity.googleid)
    implementation(libs.play.services)
    implementation(libs.androidx.datastore)
}
