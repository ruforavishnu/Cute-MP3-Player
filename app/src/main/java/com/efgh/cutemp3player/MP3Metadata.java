package com.efgh.cutemp3player;

import android.graphics.Bitmap;

/**
 * Created by Vishnu on 14-Jul-16.
 */
public class MP3Metadata
{


    int id;

    String songTitle;
    String albumTitle;
    byte[] albumArt;
    int duration;
    String path;

    public MP3Metadata()
    {

    }

    public MP3Metadata(int myId, String mySongTitle, String myAlbumTitle, byte[] myAlbumArt, int myDuration, String myPath)
    {
        this.id = myId;
        this.songTitle = mySongTitle;
        this.albumTitle = myAlbumTitle;
        this.albumArt = myAlbumArt;
        this.duration = myDuration;
        this.path = myPath;
    }

    public MP3Metadata(String mySongTitle, String myAlbumTitle, byte[] myAlbumArt, int myDuration, String myPath)
    {
        this.songTitle = mySongTitle;
        this.albumTitle = myAlbumTitle;
        this.albumArt = myAlbumArt;
        this.duration = myDuration;
        this.path = myPath;
    }
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public byte[] getAlbumArt()
    {

        return albumArt;
    }

    public void setAlbumArt(byte[] albumArt)
    {
        this.albumArt = albumArt;
    }

    public String getAlbumTitle()
    {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle)
    {
        this.albumTitle = albumTitle;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getSongTitle()
    {
        return songTitle;
    }

    public void setSongTitle(String songTitle)
    {
        this.songTitle = songTitle;
    }


}
