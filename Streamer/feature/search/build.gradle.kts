plugins {
    id("streamer.android.feature")
    id("streamer.android.library.compose")
}

android {
    namespace = "com.khalidtouch.streamer.feature.search"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
