plugins {
    id("streamer.android.library")
    id("streamer.android.hilt")
}

android {
    namespace = "com.khalidtouch.streamer.core.common"
}

dependencies {
    testImplementation(project(":core:testing"))
}