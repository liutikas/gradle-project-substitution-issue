plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("com.external:libB:1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

val projectModules = mapOf(
    "com.example:libA" to ":libA"
)

configurations.configureEach {
        resolutionStrategy.dependencySubstitution.apply {
            all {
                val requested = requested
                if (requested is ModuleComponentSelector) {
                    val module = requested.group + ":" + requested.module
                    if (projectModules.containsKey(module)) {
                        artifactSelection {
                            if (hasSelectors())
                                println("HAS SELECTORS $module ${requestedSelectors.joinToString(", ") { it.type } }")
                        }
                        artifactSelection {
                            withoutArtifactSelectors()
                        }
                        useTarget(project(projectModules[module]!!))
                    }
                }
            }
        }
    }

