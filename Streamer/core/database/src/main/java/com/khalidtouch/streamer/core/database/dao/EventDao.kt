package com.khalidtouch.streamer.core.database.dao

import android.database.SQLException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.khalidtouch.streamer.core.database.core.StreamerDatabase
import com.khalidtouch.streamer.core.database.models.Event
import com.khalidtouch.streamer.core.database.models.Song
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

@Dao
interface EventDao {
    companion object: EventDao by StreamerDatabase.Instance?.eventDao!!

    @Transaction
    @Query("SELECT Song.* FROM Event JOIN Song ON Song.id = songId GROUP BY songId ORDER BY SUM(CAST(playTime AS REAL) / (((:now - timestamp) / 86400000) + 1)) DESC LIMIT 1")
    @RewriteQueriesToDropUnusedColumns
    fun trending(now: Long = System.currentTimeMillis()): Flow<Song?>

    @Query("SELECT COUNT (*) FROM Event")
    fun eventsCount(): Flow<Int>

    @Query("DELETE FROM Event")
    fun clearEvents()

    @Query("DELETE FROM Event WHERE songId = :songId")
    fun clearEventsFor(songId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Throws(SQLException::class)
    fun insert(event: Event)
}