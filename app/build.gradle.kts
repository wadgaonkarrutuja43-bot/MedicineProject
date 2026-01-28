plugins {
    alias(libs.plugins.android.application)
    kotlin("android")
}

android {
    namespace = "com.example.medicineexpiryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.medicineexpiryapp"
        minSdk = 21
        targetSdk = 34
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

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ZXing for QR code generation and scanning
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")

    implementation("androidx.camera:camera-core:1.0.0-beta01")
    implementation("androidx.camera:camera-camera2:1.0.0-beta01")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-beta01")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}
