package com.efgh.cutemp3player;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 14-Jul-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mp3MetaDataManager";
    private static final String TABLE_MP3METADATA = "mp3MetaData";

    private static final String KEY_ID = "id";
    private static final String KEY_SONGTITLE = "songtitle";
    private static final String KEY_ALBUMTITLE = "albumtitle";
    private static final String KEY_ALBUMART = "albumart";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_PATH = "path";



    private static DatabaseHandler _instance;

    public static synchronized DatabaseHandler getInstance(Context context)
    {
        if(_instance == null)
        {
            _instance = new DatabaseHandler(context.getApplicationContext());
        }
        return _instance;
    }

    public DatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_MP3METADATA_TABLE = "CREATE TABLE " + TABLE_MP3METADATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SONGTITLE +" TEXT,"
                + KEY_ALBUMTITLE +" TEXT," + KEY_ALBUMART +" BLOB,"
                + KEY_DURATION +" INTEGER, " + KEY_PATH +" TEXT" + ")";

        db.execSQL(CREATE_MP3METADATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MP3METADATA);
        onCreate(db);

    }

    public void addMp3MetadData(MP3Metadata mData)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SONGTITLE, mData.getSongTitle());
        values.put(KEY_ALBUMTITLE, mData.getAlbumTitle());
        values.put(KEY_ALBUMART,mData.getAlbumArt());
        values.put(KEY_DURATION,mData.getDuration());
        values.put(KEY_PATH,mData.getPath());

        db.insert(TABLE_MP3METADATA, null, values);
        db.close();
    }

    public MP3Metadata getMp3MetaData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MP3METADATA,new String[]
                {
                        KEY_ID,KEY_SONGTITLE,KEY_ALBUMTITLE,KEY_ALBUMART,KEY_DURATION,KEY_PATH}
                , KEY_ID + "=?" , new String[] { String.valueOf(id) } , null,null,null,null);

        if(cursor!= null)
        {
            cursor.moveToFirst();
        }

        MP3Metadata mData = new MP3Metadata(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5));

        db.close();
        return  mData;
    }
    public List<MP3Metadata> getAllMp3MetaData()
    {
        List<MP3Metadata> mDataList = new ArrayList<MP3Metadata>();
        String SELECT_QUERY = "SELECT * FROM "+ TABLE_MP3METADATA;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if(cursor.moveToFirst())
        {
            do
            {
                MP3Metadata mData = new MP3Metadata();
                mData.setId(Integer.parseInt(cursor.getString(0)));
                mData.setSongTitle(cursor.getString(1));
                mData.setAlbumTitle(cursor.getString(2));
                mData.setAlbumArt(cursor.getBlob(3));
                mData.setDuration(Integer.parseInt(cursor.getString(4)));
                mData.setPath(cursor.getString(5));

                mDataList.add(mData);

            }while(cursor.moveToNext());
        }
        db.close();
        return mDataList;


    }

    public int getMp3MetadatasCount()
    {
        String COUNT_QUERY = "SELECT * FROM " + TABLE_MP3METADATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(COUNT_QUERY, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return  count;
    }

    public int updateMp3Metadata(MP3Metadata mData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SONGTITLE, mData.getSongTitle());
        values.put(KEY_ALBUMTITLE, mData.getAlbumTitle());
        values.put(KEY_ALBUMART,mData.getAlbumArt());
        values.put(KEY_DURATION, mData.getDuration());
        values.put(KEY_PATH, mData.getPath());

        return db.update(TABLE_MP3METADATA, values, KEY_ID + " = ? ", new String[]{String.valueOf(mData.getId())});

    }

    public void deleteMp3Metadata(MP3Metadata mData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MP3METADATA, KEY_ID + " = ? ", new String[]{String.valueOf(mData.getId())});
        db.close();


    }

}
