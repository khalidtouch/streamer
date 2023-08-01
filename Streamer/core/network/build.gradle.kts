plugins {
    id("streamer.android.library")
    id("streamer.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.khalidtouch.streamer.core.network"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    testImplementation(project(":core:testing"))
}