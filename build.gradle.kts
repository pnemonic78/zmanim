// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    sourceSets {
        main {
            java.srcDir("src")
        }
        test {
            java.srcDir("test")
        }
    }
}

dependencies {
    // Testing
    testImplementation("junit:junit:${BuildVersions.junitVersion}")
}
