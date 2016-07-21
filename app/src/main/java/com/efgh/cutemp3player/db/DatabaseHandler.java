package com.efgh.cutemp3player.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.efgh.cutemp3player.global.GlobalFunctions;
import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vishnu on 14-Jul-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mp3MetaDataManager";
    public  static final String TABLE_MP3METADATA = "mp3MetaData";

    private static final String KEY_ID = "id";
    private static final String KEY_SONGTITLE = "songtitle";
    private static final String KEY_ALBUMTITLE = "albumtitle";
    private static final String KEY_ALBUMART = "albumart";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_PATH = "path";

    private static final int MODE_READABLE = 1;
    private static final int MODE_WRITABLE = 2;



    private static DatabaseHandler _instance;
    private SQLiteDatabase db;

    public static synchronized DatabaseHandler getInstance(Context context)
    {
        if(_instance == null)
        {
            _instance = new DatabaseHandler(context.getApplicationContext());
        }
        return _instance;

    }

    public DatabaseHandler openConnection() throws SQLException
    {
        if (db == null)
        {
            db = _instance.getWritableDatabase();
        }
        return this;
    }



    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void convertArrayListToDB(ArrayList<MP3MetaData> mp3MetaDataList,Context context)
    {

        //Debug.waitForDebugger();

        DatabaseHandler instance = DatabaseHandler.getInstance(context);


        if(instance.ifDbExists() )
        {
            SQLiteDatabase db = instance.getWritableDatabase();

            instance.dropCreateAndInsert(db,mp3MetaDataList);
        }

    }

    public ArrayList<MP3MetaData>  convertDbToArrayList()
    {
        ArrayList<File> pathList = new ArrayList<File>();
        ArrayList<MP3MetaData> mp3MetaDataList = new ArrayList<MP3MetaData>();
        int totalRows = _instance.getMp3MetadatasCount();
        if(totalRows > 0)
        {
            mp3MetaDataList = _instance.getAllMp3MetaData();


            return  mp3MetaDataList;
        }

        return null;
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

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MP3METADATA);
        onCreate(db);

    }
    public void dropCreateAndInsert(SQLiteDatabase db , ArrayList<MP3MetaData> mDataList)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MP3METADATA);
        onCreate(db);
        addMp3MetadDataList(db,mDataList);


    }

    public SQLiteDatabase getDb(int mode)
    {
        if(mode == MODE_READABLE)
        {
            return this.getReadableDatabase();
        }
        else if(mode == MODE_WRITABLE)
        {
            return this.getWritableDatabase();
        }
        return null;

    }

    public boolean ifDbExists()
    {


        SQLiteDatabase db = this.getReadableDatabase();
        if(db == null)
        {
            return  false;
        }
        return true;



    }
    public String listAllTables()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String names = "";
        if(c.moveToFirst())
        {
            while(!c.moveToLast())
            {
                names += c.getString(0);


            }
            if(names.length() == 0)
            {
                names = null;
            }


        }
        else
        {
            names = null;
        }
        GlobalFunctions.log("DatabaseHandler class, listAllTables method returns names:" + names);
        return names;
    }


    public boolean ifTableExists(String tableName)
    {
        SQLiteDatabase currentDb = this.getReadableDatabase();
        boolean result = false;
        if(currentDb != null)
        {

            Cursor cursor = currentDb.rawQuery("select DISTINCT tbl_name from DATABASE_NAME where tbl_name = '"+tableName+"'", null);
            if(cursor != null)
            {
                if(cursor.getCount() > 0)
                {
                    cursor.close();
                    result = true;
                }
                else
                {
                    cursor.close();
                    result = false;
                }
            }
            else
            {
                result = false;
            }

        }
        return result;

    }

    public void addMp3MetadData(MP3MetaData mData)
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
    public void addMp3MetadDataList(SQLiteDatabase mDb ,ArrayList<MP3MetaData> mDataList)
    {


        int i = 0;
        for( i =0; i < mDataList.size(); i++)
        {

            MP3MetaData mp3MetaData = mDataList.get(i);



            ContentValues values = new ContentValues();
            values.put(KEY_SONGTITLE, mp3MetaData.getSongTitle());
            values.put(KEY_ALBUMTITLE, mp3MetaData.getAlbumTitle());
            values.put(KEY_ALBUMART,mp3MetaData.getAlbumArt());
            values.put(KEY_DURATION,mp3MetaData.getDuration());
            values.put(KEY_PATH,mp3MetaData.getPath());

            mDb.insert(TABLE_MP3METADATA, null, values);
        }
      //  Debug.waitForDebugger();

        mDb.close();
    }


    public MP3MetaData getMp3MetaData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("logtest","inside getMp3MetaData, db:"+db);

        Cursor cursor = db.query(TABLE_MP3METADATA, new String[]
                {
                        KEY_ID, KEY_SONGTITLE, KEY_ALBUMTITLE, KEY_ALBUMART, KEY_DURATION, KEY_PATH}
                , KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        Log.i("logtest","inside getMp3MetaData, cursor:"+cursor);
        if(cursor!= null)
        {
            cursor.moveToFirst();
        }



        MP3MetaData mData = new MP3MetaData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5));


        db.close();
        return  mData;
    }
    public ArrayList<MP3MetaData> getAllMp3MetaData()
    {
        ArrayList<MP3MetaData> mDataList = new ArrayList<MP3MetaData>();
        String SELECT_QUERY = "SELECT * FROM "+ TABLE_MP3METADATA;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if(cursor.moveToFirst())
        {
            do
            {
                MP3MetaData mData = new MP3MetaData();
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

    public int updateMp3Metadata(MP3MetaData mData)
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


    public void deleteMp3Metadata(MP3MetaData mData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MP3METADATA, KEY_ID + " = ? ", new String[]{String.valueOf(mData.getId())});
        db.close();


    }

}
