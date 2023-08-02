package com.khalidtouch.streamer.core.domain.extensions

import android.net.Uri
import androidx.core.net.toUri

fun Uri?.thumbnail(size: Int): Uri? {
    return toString().thumbnail(size)?.toUri()
}
