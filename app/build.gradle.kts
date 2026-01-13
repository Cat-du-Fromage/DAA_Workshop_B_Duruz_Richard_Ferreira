import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ch.heigvd.iict.daa.template"
    compileSdk = 36

    defaultConfig {
        applicationId = "ch.heigvd.iict.daa.template"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Vision APIs
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:face-detection:16.1.5")
    implementation("com.google.mlkit:image-labeling:17.0.7")
    implementation("com.google.mlkit:object-detection:17.0.0")
    implementation("com.google.mlkit:pose-detection:18.0.0-beta3")
    implementation("com.google.mlkit:segmentation-selfie:16.0.0-beta4")
    // NLP APIs
    implementation("com.google.mlkit:language-id:17.0.4")
    implementation("com.google.mlkit:translate:17.0.1")
    implementation("com.google.mlkit:smart-reply:17.0.2")
    // Custom Models
    implementation("com.google.mlkit:object-detection-custom:17.0.1")
    implementation("com.google.mlkit:image-labeling-custom:17.0.1")
    // Utilitaires
    implementation("com.google.mlkit:vision-common:17.3.0")
    implementation("com.google.mlkit:camera:16.0.0-beta3")
    // CameraX (recommandé)
    implementation("androidx.camera:camera-camera2:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")

}