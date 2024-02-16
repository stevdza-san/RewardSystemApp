plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("io.realm.kotlin")
}

android {
    namespace = "com.stevdza_san.rewardsystemapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stevdza_san.rewardsystemapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Run-time Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Google Ads
    implementation("com.google.android.gms:play-services-ads:22.6.0")
    // Ads Consent
    implementation("com.google.android.ump:user-messaging-platform:2.2.0")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Shared Preferences
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // One-Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.10")

    // Message Bar Compose
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")

    // Device Sync - MongoDB
    implementation("io.realm.kotlin:library-sync:1.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Coil Compose
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Browser
    implementation("androidx.browser:browser:1.7.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0-rc01")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.01.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}