package com.efgh.cutemp3player.metadata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.efgh.cutemp3player.R;
import com.efgh.cutemp3player.global.GlobalFunctions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 15-Jul-16.
 */
public class MetaDataRetreiver
{

    private List<File> pathList = null;


    public MetaDataRetreiver()
    {

    }


    public ArrayList<MP3MetaData> findMP3MetaDataList(List<File> pathList, Resources myResources)
    {


        ArrayList<MP3MetaData> metaDataList = new ArrayList<MP3MetaData>();
        int i = 0;
        if(pathList!=null)
        {
            try
            {

                for(i =0; i < pathList.size(); i++)
                {
                    MetaData mData = new MetaData();

                    String path = pathList.get(i).getAbsolutePath();

                    mData.initMetaData(path);


                    MP3MetaData mp3MetaData = new MP3MetaData();


                    String songTitle = mData.getSongTitle();


                    mp3MetaData.setSongTitle(songTitle);
                    String albumName = mData.getAlbumName();

                    mp3MetaData.setAlbumTitle(albumName);
                    Bitmap image = mData.getAlbumArtBitmap();



                    if(image == null)
                    {

                        image = BitmapFactory.decodeResource(myResources, R.drawable.cover);

                        ByteArrayOutputStream boStream = new ByteArrayOutputStream();

                        image.compress(Bitmap.CompressFormat.JPEG, 70, boStream);

                        byte[] art = boStream.toByteArray();

                        mp3MetaData.setAlbumArt(art);
                    }
                    else
                    {

                        ByteArrayOutputStream boStream = new ByteArrayOutputStream();

                        image.compress(Bitmap.CompressFormat.JPEG, 70, boStream);

                        byte[] art = boStream.toByteArray();

                        mp3MetaData.setAlbumArt(art);
                    }



                    mp3MetaData.setDuration(mData.getDuration());

                    mp3MetaData.setPath(mData.getMp3Path());

                    metaDataList.add(mp3MetaData);

                }

                return metaDataList;
            }
            catch (Exception e)
            {

                e.printStackTrace();
                GlobalFunctions.log("exc caught in " + this.getClass().getName());
                GlobalFunctions.log("i="+i+",path:"+pathList.get(i));
            }

        }
        return null;
    }




    public void setPathList(ArrayList<File> pathList)
    {
        this.pathList = pathList;
    }




}
