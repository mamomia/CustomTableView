import java.util.Base64

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

group = "io.github.mamomia"
version = "1.0.0"

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
        // This is deprecated
        // jvmTarget = "17"

        // ✅ Use compilerOptions instead
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
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
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.mamomia"
                artifactId = "customtableview"
                version = "1.0.0"

                pom {
                    name.set("CustomTableView")
                    description.set("A customizable table view for Android.")
                    url.set("https://github.com/mamomia/CustomTableView")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("mamomia")
                            name.set("Musharaf Islam")
                            email.set("mushi.islam007@gmail.com")
                        }
                    }

                    scm {
                        url.set("https://github.com/mamomia/CustomTableView")
                        connection.set("scm:git:git://github.com/mamomia/CustomTableView.git")
                        developerConnection.set("scm:git:ssh://github.com/mamomia/CustomTableView.git")
                    }
                }
            }
        }

        repositories {
            mavenLocal()
            // Add remote Maven repo (like Sonatype) here if needed
        }
    }

    val signingKeyFile = file("private.key")
    val signingKey = signingKeyFile.readText()
    signing {
        useInMemoryPgpKeys(
            findProperty("signing.keyId") as String?,
            signingKey,
            findProperty("signing.password") as String?
        )
        sign(publishing.publications["release"])
    }
}