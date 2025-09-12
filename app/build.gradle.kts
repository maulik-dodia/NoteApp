import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.service)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.noteapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.noteapp"
        minSdk = 24
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

// Detekt report configuration
tasks.withType<Detekt>().configureEach {
    reports {
        html.required = true
        html.outputLocation = file(path = "build/reports/detekt/detekt.html")
    }
}

// Configuring printDetektReport task, printDetektReport runs even detekt fails
tasks.named("detekt").configure {
    finalizedBy("printDetektReport")
}

// Printing detekt report location even after detekt fails
tasks.register("printDetektReport") {
    doLast {
        val reportPath = file("build/reports/detekt/detekt.html").absolutePath
        println("📊 Detekt HTML report: file://$reportPath")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Project dependencies start here
    // Compose navigation dependency
    implementation(libs.navigation.compose)

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Dagger 2 dependencies
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Firebase dependency
    implementation(libs.firebase)
}