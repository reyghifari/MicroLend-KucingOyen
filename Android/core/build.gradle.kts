plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.kucingoyen.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":entity"))
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3.android)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.appcompat)
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.viewModelCompose)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.okhttp)
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    api (libs.androidx.runtime.livedata)
    api(libs.androidx.navigation.compose)
    api(libs.coil.kt.compose)
    //Barcode Scanning
    api(libs.androidx.camera.mlkit.vision)
    api(libs.barcode.scanning)
    api(libs.logging.interceptor)
    ksp(libs.hilt.android.compiler)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.play.services)

    api(libs.kotlin.stdlib)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.animation)
    api(libs.accompanist.systemuicontroller)
    api(libs.accompanist.pager)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)



    //Retrofit
    api(libs.retrofit)
    //GSON converter
    api(libs.converter.gson)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
}
