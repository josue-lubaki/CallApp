import com.android.build.api.variant.BuildConfigField
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

androidComponents {
    val properties = Properties()
    val localFile = project.rootProject.file("local.properties")
    if(localFile.exists()){
        localFile.inputStream().use { properties.load(it) }
    }

    val streamApiKey = System.getenv("STREAM_API_KEY") ?: properties.getProperty("STREAM_API_KEY")
    val streamToken = System.getenv("STREAM_TOKEN") ?: properties.getProperty("STREAM_TOKEN")
    val streamUserID = System.getenv("STREAM_USER_ID") ?: properties.getProperty("STREAM_USER_ID")
    val streamCallID = System.getenv("STREAM_CALL_ID") ?: properties.getProperty("STREAM_CALL_ID")

    onVariants(selector().withBuildType("debug")) {
        it.buildConfigFields.put("STREAM_API_KEY",
            BuildConfigField(
                type = "String",
                value = "\"$streamApiKey\"",
                comment = "STREAM_API_KEY"
            )
        )
        it.buildConfigFields.put("STREAM_TOKEN",
            BuildConfigField(
                type = "String",
                value = "\"$streamToken\"",
                comment = "STREAM_TOKEN"
            )
        )
        it.buildConfigFields.put("STREAM_CALL_ID",
            BuildConfigField(
                type = "String",
                value = "\"$streamCallID\"",
                comment = "STREAM_CALL_ID"
            )
        )
    }

    onVariants(selector().withBuildType("release")) {
        it.buildConfigFields.put("STREAM_API_KEY",
            BuildConfigField(
                type = "String",
                value = "\"$streamApiKey\"",
                comment = "STREAM_API_KEY"
            )
        )
        it.buildConfigFields.put("STREAM_TOKEN",
            BuildConfigField(
                type = "String",
                value = "\"$streamToken\"",
                comment = "STREAM_TOKEN"
            )
        )
        it.buildConfigFields.put("STREAM_CALL_ID",
            BuildConfigField(
                type = "String",
                value = "\"$streamCallID\"",
                comment = "STREAM_CALL_ID"
            )
        )
    }
}

android {
    namespace = "ca.josue_lubaki.callapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ca.josue_lubaki.callapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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

    // navigation compose
    implementation(libs.androidx.navigation.compose)

    // io.getstream
    implementation(libs.stream.ui.compose)
    implementation(libs.stream.preview.data)

    // extended icons
    implementation(libs.androidx.icons.extended)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
}