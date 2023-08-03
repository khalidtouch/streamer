package com.khalidtouch.streamer.core.database.core

import android.os.Parcel
import androidx.media3.common.MediaItem
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
object Converters {
    @TypeConverter
    fun mediaItemFromByteArray(value: ByteArray?): MediaItem? {
        return value?.let { byteArray ->
            runCatching {
                val parcel = Parcel.obtain()
                parcel.unmarshall(byteArray, 0, byteArray.size)
                parcel.setDataPosition(0)
                val bundle = parcel.readBundle(MediaItem::class.java.classLoader)
                parcel.recycle()

                bundle?.let(MediaItem.CREATOR::fromBundle)
            }.getOrNull()
        }
    }

    @TypeConverter
    fun mediaItemToByteArray(mediaItem: MediaItem?): ByteArray? {
        return mediaItem?.toBundle()?.let { persistableBundle ->
            val parcel = Parcel.obtain()
            parcel.writeBundle(persistableBundle)
            val bytes = parcel.marshall()
            parcel.recycle()

            bytes
        }
    }
}