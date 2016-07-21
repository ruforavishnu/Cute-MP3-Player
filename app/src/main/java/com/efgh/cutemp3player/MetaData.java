package com.efgh.cutemp3player;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.beaglebuddy.id3.pojo.AttachedPicture;
import com.beaglebuddy.mp3.MP3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 07-Jul-16.
 */



public class MetaData
{
    private String songTitle="Unknown artist";
    private String albumName="Audio";
    private Bitmap albumArtBitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.cover);
    private long duration = 0;
    private String pathToFile = "";



    public String getMp3Path() {
        return pathToFile;
    }

    public void setMp3Path(String mp3Path) {
        this.pathToFile = mp3Path;
    }




    public MetaData()
    {




    }

    public void initMetaData(String path)
    {


        try {


            MP3 mp3 = new MP3(path);

            songTitle = mp3.getTitle();//track title
            if(mp3.getTitle()==null)
            {


                String songName = path;

                int index = songName.lastIndexOf("/");
                String s1 = songName.substring(index + 1, songName.length());


                songTitle = s1.substring(0, s1.length() - 4);

            }


            albumName = mp3.getAlbum();//album title
            if(  mp3.getAlbum()==null)
            {
                albumName = "Audio";
            }

            List picturesList = new ArrayList();
            picturesList = mp3.getPictures();
            byte[] art;
            if(picturesList != null)
            {
                if(picturesList.size() > 0)
                {

                    AttachedPicture pic = (AttachedPicture)picturesList.get(0);
                    if(pic != null)
                    {

                        art = pic.getImage();
                    }
                    else
                    {
                        Bitmap defaultpic = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.cover);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        defaultpic.compress(Bitmap.CompressFormat.JPEG,70,bos);
                        art = bos.toByteArray();
                        albumArtBitmap =  BitmapFactory.decodeByteArray(art, 0, art.length);//album art bmp
                    }
                }
            }
            else
            {
                Bitmap defaultpic = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.cover);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                defaultpic.compress(Bitmap.CompressFormat.JPEG,70,bos);
                art = bos.toByteArray();
                albumArtBitmap =  BitmapFactory.decodeByteArray(art, 0, art.length);//album art bmp

            }


            MediaMetadataRetriever metaRetreiver = new MediaMetadataRetriever();
            metaRetreiver.setDataSource(path);
            duration = Long.parseLong(metaRetreiver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));//track duration

            pathToFile = path;//path to music file
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public MetaData findMetaData(String path)
    {
        MetaData mData = new MetaData();
        mData.initMetaData(path);

        return mData;
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

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public String getPathToFile()
    {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile)
    {
        this.pathToFile = pathToFile;
    }
}
