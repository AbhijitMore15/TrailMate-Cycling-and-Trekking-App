plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")   // Required for Kotlin 2.x
    //id("com.mappls.services.android")           // Mappls SDK plugin
}

android {
    namespace = "com.trailmate.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.trailmate.app"
        minSdk = 26
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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

    // ---------- MAP ENGINES ----------
    //implementation("com.mappls.sdk:mappls-android-sdk:9.0.0")
    implementation("org.osmdroid:osmdroid-android:6.1.18") // free offline maps

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
    implementation("androidx.compose.material:material-icons-extended")

    // ---------- NAVIGATION ----------
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ---------- VIEWMODEL ----------
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

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
    implementation(libs.androidx.compose.foundation)

    // ---------- TEST ----------
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
