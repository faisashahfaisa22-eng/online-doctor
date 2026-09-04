plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.onlinedoctor.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.onlinedoctor.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.1.0"
    }
}
