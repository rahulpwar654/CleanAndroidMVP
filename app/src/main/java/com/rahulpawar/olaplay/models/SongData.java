package com.rahulpawar.olaplay.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by warlord on 12/17/2017.
 */


@Entity
public class SongData implements Serializable{


    public boolean isPlaying =false;

    public static DiffCallback<SongData> DIFF_CALLBACK = new DiffCallback<SongData>() {
        @Override
        public boolean areItemsTheSame(@NonNull SongData oldItem, @NonNull SongData newItem) {
            return oldItem.songid == newItem.songid;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SongData oldItem, @NonNull SongData newItem) {
            return oldItem.equals(newItem);
        }
    };
    /**
     * {
     song: "Afreen Afreen",
     url: "http://hck.re/Rh8KTk",
     artists: "Rahat Fateh Ali Khan, Momina Mustehsan",
     cover_image: "http://hck.re/kWWxUI"
     }
     */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    public long songid;


    @SerializedName("song")
    @ColumnInfo(name = "song")
    @Expose
    public String song;

    @SerializedName("url")
    @ColumnInfo(name = "url")
    @Expose
    public String url;


    @SerializedName("artists")
    @ColumnInfo(name = "artists")
    @Expose
    public String artists="";


    @SerializedName("cover_image")
    @ColumnInfo(name = "cover_image")
    @Expose
    public String cover_image;


    @ColumnInfo(name = "fevorite" )
    public boolean fevorite=false;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SongData songData = (SongData) o;

        if (!song.equals(songData.song)) return false;
        if (url != null ? !url.equals(songData.url) : songData.url != null) return false;
        if (!artists.equals(songData.artists)) return false;
        return cover_image != null ? cover_image.equals(songData.cover_image) : songData.cover_image == null;
    }

    @Override
    public int hashCode() {
        int result = song.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + artists.hashCode();
        result = 31 * result + (cover_image != null ? cover_image.hashCode() : 0);
        return result;
    }
}
