package com.khalidtouch.streamer.convention

@Suppress("unused")
enum class StreamerBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}