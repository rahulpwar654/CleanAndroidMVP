package com.rahulpawar.olaplay.roomdb;

import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.rahulpawar.olaplay.models.SongData;

import java.util.List;


@Dao
public interface SongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<SongData> songData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(SongData... songData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateSong(SongData... songData);

    @Delete
    public void deleteSong(SongData... songData);

    @Query("DELETE FROM SongData")
    public void nukeTable();

    @Query("SELECT * FROM SongData")
    public abstract List<SongData> songDataBysong();

    @Query("SELECT * FROM SongData Where SongData.song Like :search")
    public abstract List<SongData> songsSearch(String search);


    @Query("SELECT * FROM SongData")
    public abstract LivePagedListProvider<Integer,SongData> songsDataBySong();

    /**
     *
     *  @Query("SELECT note.id, note.title, note.description, note.category_id " +
    "FROM note " +
    "LEFT JOIN category ON note.category_id = category.id " +
    "WHERE note.id = :noteId")
    CategoryNote getCategoryNote(long noteId);
     *
     */

}
