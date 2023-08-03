package com.khalidtouch.streamer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khalidtouch.streamer.core.database.core.StreamerDatabase
import com.khalidtouch.streamer.core.database.models.Format
import kotlinx.coroutines.flow.Flow

@Dao
interface FormatDao {
    companion object: FormatDao by StreamerDatabase.Instance?.formatDao!!

    @Query("SELECT * FROM Format WHERE songId = :songId")
    fun format(songId: String): Flow<Format?>

    @Query("SELECT loudnessDb FROM Format WHERE songId = :songId")
    fun loudnessDb(songId: String): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(format: Format)
}