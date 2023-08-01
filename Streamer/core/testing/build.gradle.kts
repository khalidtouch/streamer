plugins {
    id("streamer.android.library")
    id("streamer.android.library.compose")
    id("streamer.android.hilt")
}

android {
    namespace = "com.khalidtouch.streamer.core.testing"
}

dependencies {
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.espresso)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.hilt.android.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.tracing)
    debugApi(libs.androidx.compose.ui.test.manifest)
}