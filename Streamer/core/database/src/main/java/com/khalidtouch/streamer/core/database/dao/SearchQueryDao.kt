package com.khalidtouch.streamer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khalidtouch.streamer.core.database.core.StreamerDatabase
import com.khalidtouch.streamer.core.database.models.SearchQuery
import com.khalidtouch.streamer.core.database.models.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchQueryDao {
    companion object: SearchQueryDao by StreamerDatabase.Instance?.searchQueryDao!!

    @Query("SELECT * FROM SearchQuery WHERE query LIKE :query ORDER BY id DESC")
    fun queries(query: String): Flow<List<SearchQuery>>

    @Query("SELECT COUNT (*) FROM SearchQuery")
    fun queriesCount(): Flow<Int>

    @Query("DELETE FROM SearchQuery")
    fun clearQueries()

    @Query("SELECT * FROM Song WHERE title LIKE :query OR artistsText LIKE :query")
    fun search(query: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchQuery: SearchQuery)

    @Delete
    fun delete(searchQuery: SearchQuery)
}