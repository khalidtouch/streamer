package com.khalidtouch.streamer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khalidtouch.streamer.core.database.core.StreamerDatabase
import com.khalidtouch.streamer.core.database.models.QueuedMediaItem

@Dao
interface QueuedMediaItemDao {
    companion object : QueuedMediaItemDao by StreamerDatabase.Instance?.queuedMediaItemDao!!

    @Query("SELECT * FROM QueuedMediaItem")
    fun queue(): List<QueuedMediaItem>

    @Query("DELETE FROM QueuedMediaItem")
    fun clearQueue()

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(queuedMediaItems: List<QueuedMediaItem>)
}