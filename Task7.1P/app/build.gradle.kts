plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lostandfoundapp"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.lostandfoundapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Room for database persistence
    val room_version = "2.6.1" // Check for the latest version
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")

    // ViewModel and LiveData for MVVM
    val lifecycle_version = "2.7.0" // Check for the latest version
    implementation ("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")
    implementation ("androidx.activity:activity:1.8.2")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}