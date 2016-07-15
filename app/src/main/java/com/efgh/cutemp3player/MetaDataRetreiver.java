package com.efgh.cutemp3player;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
        if(pathList!=null)
        {
            try
            {
                Log.i("logtest", "pathlist size:" + pathList.size());
                for(int i =0; i < pathList.size(); i++)
                {
                    MetaData mData = new MetaData();
                    String path = pathList.get(i).getAbsolutePath();
                    mData.initMetaData(path);

                    MP3MetaData mp3MetaData = new MP3MetaData();

                    mp3MetaData.setSongTitle(mData.getSongTitle());
                    mp3MetaData.setAlbumTitle(mData.getAlbumName());
                    Bitmap image = mData.getAlbumArtBitmap();

                    if(image == null)
                    {
                        image = BitmapFactory.decodeResource(myResources,R.drawable.cover);
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
                Log.i("logtest", "inside metadataretreiver , metaDataList size:" + metaDataList.size());
                return metaDataList;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public void setPathList(List<File> pathList)
    {
        this.pathList = pathList;
    }




}
