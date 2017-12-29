package com.rahulpawar.olaplay.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.rahulpawar.olaplay.models.SongData;



@Database(entities = {SongData.class}, version = 1 ,exportSchema = false)
abstract public class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "SongsDB";

    public abstract SongsDao songsDao();
}
