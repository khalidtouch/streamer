plugins {
    id("streamer.android.library")
    id("streamer.android.hilt")
}

android {
    namespace = "com.khalidtouch.streamer.core.data"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    testImplementation(project(":core:testing"))
}