plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "net.app.nfusion.blurlayoutlibrary"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.app.nfusion.blurlayoutlibrary"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled =
            true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Glide dependencies
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Blur-Layout library
    implementation(project(":blurlibrary"))
}
