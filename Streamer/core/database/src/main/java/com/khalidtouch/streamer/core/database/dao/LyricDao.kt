package com.khalidtouch.streamer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.khalidtouch.streamer.core.database.core.StreamerDatabase
import com.khalidtouch.streamer.core.database.models.Lyrics
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricDao {
    companion object: LyricDao by StreamerDatabase.Instance?.lyricDao!!

    @Query("SELECT * FROM Lyrics WHERE songId = :songId")
    fun lyrics(songId: String): Flow<Lyrics?>

    @Upsert
    fun upsert(lyrics: Lyrics)
}