plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.locationtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.locationtracker"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding= true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.androidx.material3.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.circleimageview)
    implementation(platform(libs.firebase.bom)) // Firebase BOM removed
    // implementation(libs.firebase.analytics) // Firebase Analytics removed
    // implementation(libs.firebase.database) // Firebase Realtime Database removed
    // implementation(libs.firebase.auth) // Firebase Authentication removed
    // implementation(libs.firebase.dynamic.links) // Firebase Dynamic Links removed

    implementation(libs.play.services.location.v1800)
    implementation(libs.androidx.preference.ktx)

    // Firebase Functions and related dependencies removed
    // implementation(libs.google.firebase.functions.ktx)
    // implementation(libs.firebase.analytics.ktx)
    // implementation(libs.firebase.dynamic.links.ktx)
    // implementation(libs.com.google.firebase.firebase.functions.ktx)
    // implementation(libs.firebase.functions.ktx.v2010)

    implementation(libs.gson)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.play.services.location) // Example for Location
    implementation(libs.androidx.work.runtime.ktx.v281)

    implementation(libs.okhttp)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.logging.interceptor)

    implementation(libs.kotlinx.serialization.json)

    implementation (libs.okhttp.v490)

    implementation (libs.glide)
    annotationProcessor (libs.compiler)


}

