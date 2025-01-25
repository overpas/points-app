plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.pointstesttask"
    compileSdk = properties["target.sdk"].toString().toInt()

    defaultConfig {
        applicationId = "com.example.pointstesttask"
        minSdk = properties["min.sdk"].toString().toInt()
        targetSdk = properties["target.sdk"].toString().toInt()
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
        sourceCompatibility = JavaVersion.toVersion(properties["jvm.target"].toString())
        targetCompatibility = JavaVersion.toVersion(properties["jvm.target"].toString())
    }
    kotlinOptions {
        jvmTarget = properties["jvm.target"].toString()
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
}