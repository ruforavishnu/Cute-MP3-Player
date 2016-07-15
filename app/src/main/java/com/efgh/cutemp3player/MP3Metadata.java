package com.efgh.cutemp3player;

/**
 * Created by Vishnu on 14-Jul-16.
 */
public class MP3MetaData
{


    int id;

    String songTitle;
    String albumTitle;
    byte[] albumArt;
    long duration;
    String path;

    public MP3MetaData()
    {

    }

    public MP3MetaData(int myId, String mySongTitle, String myAlbumTitle, byte[] myAlbumArt, int myDuration, String myPath)
    {
        this.id = myId;
        this.songTitle = mySongTitle;
        this.albumTitle = myAlbumTitle;
        this.albumArt = myAlbumArt;
        this.duration = myDuration;
        this.path = myPath;
    }

    public MP3MetaData(String mySongTitle, String myAlbumTitle, byte[] myAlbumArt, int myDuration, String myPath)
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

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
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
