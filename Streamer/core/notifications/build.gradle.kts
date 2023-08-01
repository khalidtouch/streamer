plugins {
    id("streamer.android.library")
    id("streamer.android.hilt")
}

android {
    namespace = "com.khalidtouch.streamer.core.notifications"
}

dependencies {

   implementation(libs.androidx.compose.runtime)
}