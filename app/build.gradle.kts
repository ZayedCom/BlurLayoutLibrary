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
        renderscriptTargetApi = 21  // Target RenderScript API version (21 or above)
        renderscriptSupportModeEnabled =
            true  // Enable support for RenderScript on devices that don't support it natively

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

    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    implementation(project(":blurlibrary"))
    implementation(project(":blurlibrary"))
}
