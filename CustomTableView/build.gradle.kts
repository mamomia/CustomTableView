import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("maven-publish")
    id("signing")
}

group = "io.github.mamomia"
version = "1.0.2"

android {
    namespace = "com.mushi.customtableview"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        consumerProguardFiles("consumer-rules.pro")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    extensions.configure<SigningExtension>("signing") {
        useGpgCmd()
        sign(publishing.publications)
    }
}

mavenPublishing {

    publishToMavenCentral()
    signAllPublications()

    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true
        )
    )

    pom {
        name.set("CustomTableView")
        description.set("A custom table view library for Android.")
        inceptionYear.set("2025")
        url.set("https://github.com/mamomia/CustomTableView")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("mamomia")
                name.set("Musharaf Islam")
                url.set("https://github.com/mamomia")
            }
        }
        scm {
            url.set("https://github.com/mamomia/CustomTableView")
            connection.set("scm:git:https://github.com/mamomia/CustomTableView.git")
            developerConnection.set("scm:git:ssh://git@github.com/mamomia/CustomTableView.git")
        }
    }
}