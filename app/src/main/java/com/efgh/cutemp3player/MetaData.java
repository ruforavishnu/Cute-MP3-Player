package com.efgh.cutemp3player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.beaglebuddy.id3.pojo.AttachedPicture;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;

import com.beaglebuddy.mp3.MP3;
import com.beaglebuddy.id3.enums.PictureType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 07-Jul-16.
 */



public class MetaData
{
    private String songTitle="Unknown artist", albumName="Audio";
    private Bitmap albumArtBitmap;

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    private String mp3Path;

    MediaMetadataRetriever metaRetreiver;

    public boolean AsyncTaskCompleted = false;



    public MetaData(String valueInDataset)
    {
        mp3Path = valueInDataset;


        try
        {


            MP3 mp3 = new MP3(mp3Path);

            List picturesList = new ArrayList();
            picturesList = mp3.getPictures();
            AttachedPicture pic = (AttachedPicture)picturesList.get(0);

            byte[] art = pic.getImage();
            if (art != null)
            {
                albumArtBitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            }
            songTitle = mp3.getTitle();
            albumName = mp3.getAlbum();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public String getSongTitle()
    {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Bitmap getAlbumArtBitmap()
    {

        return albumArtBitmap;
    }

    public void setAlbumArtBitmap(Bitmap albumArtBitmap) {
        this.albumArtBitmap = albumArtBitmap;
    }
}
