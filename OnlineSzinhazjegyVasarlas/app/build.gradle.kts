plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.onlineszinhazjegyvasarlas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.onlineszinhazjegyvasarlas"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.glide)
    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /*implementation 'androidx.recyclerview:recyclerview:1.3.2'*/
   /* implementation "androidx.recyclerview:recyclerview-selection:1.1.0"

    implementation "androidx.cardview:cardview:1.0.0"*/
//    implementation ‘com.github.bumptech.glide:glide:4.11.0’

}