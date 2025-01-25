import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.example.pointsapp"
    compileSdk = properties["target.sdk"].toString().toInt()

    defaultConfig {
        applicationId = "com.example.pointsapp"
        minSdk = properties["min.sdk"].toString().toInt()
        targetSdk = properties["target.sdk"].toString().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"${getBaseUrl()}\"")
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
    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    sourceSets.all {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.adapters.result)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.converter.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

private fun getBaseUrl(): String {
    return if (System.getenv("CI") == "true") {
        System.getenv("BASE_URL")
    } else {
        val localProperties = Properties().apply {
            File(rootDir, "local.properties")
                .takeIf(File::exists)
                ?.let(File::inputStream)
                ?.let(::load)
        }
        return localProperties.getProperty("base.url")
            ?: error("Can't find 'base.url' property in local.properties file")
    }
}
