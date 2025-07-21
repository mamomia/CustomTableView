plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

group = "io.github.mamomia"
version = "1.0.0"

android {
    namespace = "com.mushi.customtableview"
    compileSdk = 36

    defaultConfig {
        minSdk = 19

        vectorDrawables {
            useSupportLibrary = true
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "17"
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
            maven {
                name = "mavenCentral"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = findProperty("centralUsername") as String?
                    password = findProperty("centralToken") as String?
                }
            }
        }
    }
}