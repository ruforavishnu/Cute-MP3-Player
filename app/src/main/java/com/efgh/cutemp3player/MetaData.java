package com.efgh.cutemp3player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

/**
 * Created by Vishnu on 07-Jul-16.
 */



public class MetaData
{
    private String songTitle="No title found", albumName="No album found";
    private Bitmap albumArtBitmap;//TODO: initialize albumArt to default res album art here itself

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }

    private String mp3Path;

    MediaMetadataRetriever metaRetreiver;
    private Context mContext;
    public boolean AsyncTaskCompleted = false;



    public MetaData(String valueInDataset, Context myContext)
    {
        mp3Path = valueInDataset;
        mContext = myContext;

        try
        {

            metaRetreiver = new MediaMetadataRetriever();
            metaRetreiver.setDataSource(mp3Path);
            byte[] art = metaRetreiver.getEmbeddedPicture();
            if (art != null)
            {
                albumArtBitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            } else
            {
                albumArtBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cover);
            }
            Mp3File mp3file = null;
            mp3file = new Mp3File(mp3Path);
            if(mp3file.hasId3v1Tag())
            {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                if(id3v1Tag!=null)
                {

                    songTitle = id3v1Tag.getTitle().length() > 0 ? id3v1Tag.getTitle() : "No title found";
                    albumName = id3v1Tag.getAlbum().length() > 0 ? id3v1Tag.getAlbum() : "No album found";
                }

            }
            if (mp3file.hasId3v2Tag())
            {

                ID3v1 id3v2Tag = mp3file.getId3v1Tag();
                if(id3v2Tag!=null)
                {
                    songTitle = id3v2Tag.getTitle().length() > 0 ? id3v2Tag.getTitle() : "No title found";
                    albumName = id3v2Tag.getAlbum().length() > 0 ? id3v2Tag.getAlbum() : "No album found";
                }

            }

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

    public Bitmap getAlbumArtBitmap() {
        return albumArtBitmap;
    }

    public void setAlbumArtBitmap(Bitmap albumArtBitmap) {
        this.albumArtBitmap = albumArtBitmap;
    }
}
