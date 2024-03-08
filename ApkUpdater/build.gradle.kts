plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.supersuman.apkupdater"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    testImplementation("junit:junit:4.13.2")

}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}