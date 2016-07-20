package com.efgh.cutemp3player;

import android.content.Context;
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
        writeDbWithArrayList(mp3MetaDataList);

    }
    public void writeDbWithArrayList(List<MP3MetaData> mp3MetaDataList)
    {
        _instance.openConnection();
        SQLiteDatabase db = _instance.getWritableDatabase();

        _instance.dropCreateAndInsert(db, mp3MetaDataList);

    }
    public List<MP3MetaData>  convertDbToArrayList()
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
