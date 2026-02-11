plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")   // ⭐ REQUIRED FOR KOTLIN 2.0+
    // Mappls services Gradle plugin (required when using .a.conf / .a.olf configuration)
    id("com.mappls.services.android")
}

android {
    namespace = "com.trailmate.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.trailmate.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true   // ⭐ REQUIRED
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // ---------- MAPPLS MAP SDK ----------
    // Use explicit version; repo is already declared in settings.gradle.kts
    implementation("com.mappls.sdk:mappls-android-sdk:9.0.0")

    // ---------- CORE ----------
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // ---------- COMPOSE ----------
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // Material icons (Filled, e.g. Map, Logout, DirectionsRun)
    implementation("androidx.compose.material:material-icons-extended")

    // ---------- NAVIGATION ----------
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ---------- NETWORK ----------
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // ---------- COROUTINES ----------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ---------- JSON ----------
    implementation("com.google.code.gson:gson:2.10.1")

    // ---------- LOCATION ----------
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // ---------- DATASTORE ----------
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // ---------- VIEWMODEL ----------
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // ---------- TEST ----------
    testImplementation("junit:junit:4.13.2")

    // Instrumentation / UI tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
