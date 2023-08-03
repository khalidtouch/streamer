package com.khalidtouch.streamer.core.database.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.khalidtouch.streamer.core.database.dao.AlbumDao
import com.khalidtouch.streamer.core.database.dao.ArtistDao
import com.khalidtouch.streamer.core.database.dao.EventDao
import com.khalidtouch.streamer.core.database.dao.FormatDao
import com.khalidtouch.streamer.core.database.dao.LyricDao
import com.khalidtouch.streamer.core.database.dao.PlaylistDao
import com.khalidtouch.streamer.core.database.dao.QueuedMediaItemDao
import com.khalidtouch.streamer.core.database.dao.RawDao
import com.khalidtouch.streamer.core.database.dao.SearchQueryDao
import com.khalidtouch.streamer.core.database.dao.SongDao
import com.khalidtouch.streamer.core.database.models.Album
import com.khalidtouch.streamer.core.database.models.Artist
import com.khalidtouch.streamer.core.database.models.Event
import com.khalidtouch.streamer.core.database.models.Format
import com.khalidtouch.streamer.core.database.models.Lyrics
import com.khalidtouch.streamer.core.database.models.Playlist
import com.khalidtouch.streamer.core.database.models.QueuedMediaItem
import com.khalidtouch.streamer.core.database.models.SearchQuery
import com.khalidtouch.streamer.core.database.models.Song
import com.khalidtouch.streamer.core.database.models.SongAlbumMap
import com.khalidtouch.streamer.core.database.models.SongArtistMap
import com.khalidtouch.streamer.core.database.models.SongPlaylistMap
import com.khalidtouch.streamer.core.database.models.SortedSongPlaylistMap

@Database(
    entities = [
        Song::class,
        SongPlaylistMap::class,
        Playlist::class,
        Artist::class,
        SongArtistMap::class,
        Album::class,
        SongAlbumMap::class,
        SearchQuery::class,
        QueuedMediaItem::class,
        Format::class,
        Event::class,
        Lyrics::class,
    ],
    views = [
        SortedSongPlaylistMap::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class StreamerDatabase protected constructor(): RoomDatabase() {

    abstract val songDao: SongDao
    abstract val albumDao: AlbumDao
    abstract val artistDao: ArtistDao
    abstract val eventDao: EventDao
    abstract val formatDao: FormatDao
    abstract val lyricDao: LyricDao
    abstract val playlistDao: PlaylistDao
    abstract val queuedMediaItemDao: QueuedMediaItemDao
    abstract val rawDao: RawDao
    abstract val searchQueryDao: SearchQueryDao

    companion object {
        @Volatile
        var Instance: StreamerDatabase? = null

        operator fun invoke(context: Context): StreamerDatabase {
            return Instance ?: synchronized(this) {
               val instance = Room
                    .databaseBuilder(context.applicationContext, StreamerDatabase::class.java, "streamer.db")
                    .build()
                Instance = instance
                instance
            }
        }
    }
}