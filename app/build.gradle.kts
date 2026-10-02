import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    // On the build classpath but applied conditionally below (task 51.1).
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

// Apply the Firebase Gradle plugins only when a google-services.json is present,
// so the project still builds without Firebase credentials (task 51.1).
// Per-environment config (task 51): the debug build (applicationId
// com.carloserp.android.debug) reads app/src/debug/google-services.json (TEST
// project), the release build (com.carloserp.android) reads
// app/src/release/google-services.json (PRODUCTION). A single app/google-services.json
// also works as a fallback for all variants. All of these are gitignored.
// The Crashlytics plugin is REQUIRED whenever the Crashlytics SDK is active: it
// injects the build-id resource the SDK reads at startup (without it the app
// crashes in FirebaseInitProvider).
val googleServicesConfigs = listOf(
    "google-services.json",
    "src/debug/google-services.json",
    "src/release/google-services.json",
)
if (googleServicesConfigs.any { file(it).exists() }) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

// Google Maps SDK key, read from local.properties (MAPS_API_KEY=...) so it stays
// out of version control. Falls back to an empty string: the app still builds
// and runs, the map tiles just stay blank until a real key is supplied.
val mapsApiKey: String = run {
    val props = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { props.load(it) }
    }
    props.getProperty("MAPS_API_KEY", "")
}

android {
    namespace = "com.carloserp.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.carloserp.android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            // TEST backend (Railway). Retrofit base URL MUST end with '/'.
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://carlos-backend-production.up.railway.app/api/v1/\"",
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://carlos-backend-production.up.railway.app/api/v1/\"",
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
        buildConfig = true
    }
}

dependencies {
    // AndroidX + Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.navigation.compose)

    // Hilt (DI) — see task 49.3
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Networking (Retrofit/OkHttp/kotlinx.serialization) — see task 49.2
    implementation(libs.bundles.networking)
    implementation(libs.kotlinx.coroutines.android)

    // Room (offline-first prep) — see task 49.4
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Secure token storage (encrypted) + preferences — see task 50.1
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.datastore.preferences)

    // Biometric sign-in (BiometricPrompt + Keystore-backed credential storage)
    implementation(libs.androidx.biometric)

    // Google Maps (Compose) — red pin on the validated address
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    // Firebase (Analytics / Crashlytics / Performance) — see task 51. Versions
    // come from the BOM. Works with graceful no-op fallback until a
    // google-services.json is provided (then the plugin above activates it).
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)

    // Compose tooling (debug only)
    debugImplementation(libs.compose.ui.tooling)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented / UI tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}
