package com.khalidtouch.streamer.core.database.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.khalidtouch.streamer.core.database.core.StreamerDatabase

@Dao
interface RawDao {
    @RawQuery
    fun raw(supportSQLiteQuery: SupportSQLiteQuery): Int

    fun checkpoint() {
        raw(SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)"))
    }

    companion object: RawDao by StreamerDatabase.Instance?.rawDao!!
}