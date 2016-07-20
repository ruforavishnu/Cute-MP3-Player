package com.efgh.cutemp3player;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 15-Jul-16.
 */
public class DBConversion
{
    private DatabaseHandler _instance;
    private final String TABLE_MP3METADATA = "mp3MetaData";
    public DBConversion(DatabaseHandler _handlerInstance)
    {
        this._instance = _handlerInstance;
    }
    public void convertArrayListToDB(List<MP3MetaData> mp3MetaDataList)
    {
        if(_instance.ifDbExists() && _instance.ifTableExists(TABLE_MP3METADATA))
        {
            SQLiteDatabase db = _instance.getWritableDatabase();
            for(int i = 0 ; i < mp3MetaDataList.size(); i++)
            {
                MP3MetaData mp3MetaData = mp3MetaDataList.get(i);
                String songTitle = mp3MetaData.getSongTitle();
                String albumTitle = mp3MetaData.getAlbumTitle();
                byte[] artInBytes = mp3MetaData.getAlbumArt();
                long songDuration = mp3MetaData.getDuration();
                String pathToSong = mp3MetaData.getPath();



            }
            _instance.dropCreateAndInsert(db,mp3MetaDataList);
        }
    }
    public List<MP3MetaData>  convertDbToArrayList(SQLiteDatabase db)
    {
        List<File> pathList = new ArrayList<File>();
        List<MP3MetaData> mp3MetaDataList = new ArrayList<MP3MetaData>();
        int totalRows = _instance.getMp3MetadatasCount();
        if(totalRows > 0)
        {
            mp3MetaDataList = _instance.getAllMp3MetaData();


            return  mp3MetaDataList;
        }

       return null;
    }
}