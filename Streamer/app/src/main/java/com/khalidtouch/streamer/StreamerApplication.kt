package com.khalidtouch.streamer

import android.app.Application
import com.khalidtouch.streamer.core.database.core.StreamerDatabase

class StreamerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StreamerDatabase(this)
    }
}