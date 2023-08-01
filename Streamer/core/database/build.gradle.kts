plugins {
    id("streamer.android.library")
    id("streamer.android.hilt")
    id("streamer.android.room")
}

android {
    namespace = "com.khalidtouch.streamer.core.database"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    testImplementation(project(":core:testing"))
}