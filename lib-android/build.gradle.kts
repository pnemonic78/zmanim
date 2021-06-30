plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles("proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    lintOptions {
        disable("SimpleDateFormat")
    }

    sourceSets {
        getByName("main") {
            java.srcDir("../src")
        }
        getByName("test") {
            java.srcDir("../test")
        }
    }
}

dependencies {
    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
}
