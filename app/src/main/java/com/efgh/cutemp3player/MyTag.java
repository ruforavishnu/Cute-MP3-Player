package com.efgh.cutemp3player;

/**
 * Created by Vishnu on 08-Jul-16.
 */

public class MyTag
{
    String  mp3FilePath;

    public String getMp3FilePath() {
        return mp3FilePath;
    }

    public void setMp3FilePath(String mp3FilePath) {
        this.mp3FilePath = mp3FilePath;
    }

    public MyTag()
    {
        mp3FilePath = null;
    }

    public MyTag(String path)
    {
        mp3FilePath = path;
    }


}