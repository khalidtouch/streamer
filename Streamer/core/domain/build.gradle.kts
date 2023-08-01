plugins {
    id("streamer.android.library")
    kotlin("kapt")
}

android {
    namespace = "com.khalidtouch.streamer.core.domain"
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    testImplementation(project(":core:testing"))
}