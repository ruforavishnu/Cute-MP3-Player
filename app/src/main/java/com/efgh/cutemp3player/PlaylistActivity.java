package com.efgh.cutemp3player;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.widget.AbsListView;

import com.mobeta.android.demodslv.CursorDSLV;


public class PlaylistActivity extends AppCompatActivity {

    private int REQ_CODE_PICK_SOUNDFILE = 11;
    private RecyclerView playList;

    private RecycleViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private ProgressDialog progressDialog;
    TextView statusTextView;

    private List<File> allFolders;
    private List<String> allFoldersPathAsString ;
    private String[] allFoldersStringArray;

    private List<String> pathList;
    private List<Bitmap> imageList;
    private List<String> songTitleList;
    private List<String> albumNameList;

    private DatabaseHandler dbHandler;


    private long nanoStartTime;
    private long nanaEndTime;

    private List<File> musicFilesList;

    private List<MP3MetaData> mp3MetaDataList;


    public  Bitmap getDefaultBitmap()
    {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.cover);
        return  bmp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = DatabaseHandler.getInstance(getBaseContext());
        dbHandler.openConnection();



        Intent i = new Intent(this, CursorDSLV.class);
        startActivity(i);



       /* progressDialog = new ProgressDialog(getApplicationContext());


        playList = (RecyclerView)findViewById(R.id.recycler_view);


        dbHandler = DatabaseHandler.getInstance(getApplicationContext());

       *//* lazy load*//*//////////////////////////////////////////////////
        ArrayList<MP3MetaData> mDataList = new ArrayList<MP3MetaData>();
        for(int i = 1 ; i<=12; i++)
        {
            MP3MetaData m = dbHandler.getMp3MetaData(i);
            mDataList.add(m);
        }
        int count = dbHandler.getMp3MetadatasCount();
        Log.i("logtest","mDataList count:"+count);
        mLayoutManager = new LinearLayoutManager(this);
        playList.setLayoutManager(mLayoutManager);

        mAdapter = new RecycleViewAdapter(mDataList);

        playList.setAdapter(mAdapter);
        playList.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                customLoadMoreItemsToAdapter(current_page);
            }
        });
        //////////////////////////////////////////////////////////////

       *//* LazyLoadAsyncTask task = new LazyLoadAsyncTask();
        task.execute(mAdapter);
        Log.i("logtest","playlist adapter set");
*//*

        ;
        *//*RescanMusicAsyncTask rescanTask = new RescanMusicAsyncTask();
        rescanTask.execute();*//*

        nanaEndTime = System.nanoTime();


        long timeTaken = nanaEndTime - nanoStartTime;

        long timeInSecs = TimeUnit.NANOSECONDS.toSeconds(timeTaken);
        Log.i("logtest", "time taken for exec:" + timeInSecs);
*/





    }

    public void customLoadMoreItemsToAdapter(int page)
    {
        int startOffset = page * 6;
        int threshold = (page* 6) + 6;

        for(int i = startOffset ; i<=threshold; i++)
        {
            MP3MetaData m = dbHandler.getMp3MetaData(i);

            mAdapter.add(i,m);
        }
        mAdapter.notifyDataSetChanged();


    }

    public class  LazyLoadAsyncTask extends AsyncTask
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            int count = mAdapter.getItemCount();
            Toast.makeText(getApplicationContext(),"starting lazy load,current child count:"+count,Toast.LENGTH_LONG).show();
        }




        @Override
        protected Object doInBackground(Object[] params)
        {
            int count = dbHandler.getMp3MetadatasCount();


            for(int i = 1 ; i<=count; i++)
            {
                MP3MetaData m = dbHandler.getMp3MetaData(i);

                mAdapter.add(i,m);
            }
            mAdapter.notifyDataSetChanged();



            return null;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            int count = mAdapter.getItemCount();
            Toast.makeText(getApplicationContext(),"Lazy load completed, current child count:"+count,Toast.LENGTH_LONG).show();
        }

    }
    public class RescanMusicAsyncTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i("logtest","inside doInBackground");
            musicFilesList = RescanMusic.findAllMusicFiles();
            Log.i("logtest", "inside doInBackground RescanMusic.findAllMusicFiles() completed");
            MetaDataRetreiver mDataRetreiver = new MetaDataRetreiver();
            Log.i("logtest","inside doInBackground mDataRetreiver instance  created");
            mp3MetaDataList = mDataRetreiver.findMP3MetaDataList(musicFilesList,getResources());
            Log.i("logtest","inside doInBackground findMP3MetaDataList,mp3MetaDataList size: "+mp3MetaDataList.size());
            DatabaseHandler _dbInstance = DatabaseHandler.getInstance(getApplicationContext());
            Log.i("logtest","inside doInBackground _dbInstance created ");
            DBConversion dbConversion = new DBConversion(_dbInstance);
            Log.i("logtest","inside doInBackground dbConversion instance created ");
            dbConversion.convertArrayListToDB(mp3MetaDataList);
            Log.i("logtest", "inside doInBackground converted arraylist to db");

            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);


            renderRecyclerView();

        }
    }


    private void renderRecyclerView()
    {

        mLayoutManager = new CustomLayoutManager(this);
        playList.setLayoutManager(mLayoutManager);

        mAdapter = new RecycleViewAdapter(mp3MetaDataList);

        playList.setAdapter(mAdapter);
        Log.i("logtest","playlist adapter set");
    }



    /*public void loadPlaylist()
    {
        try {




            List<File> mp3FilesList = new ArrayList<File>();
            String rootFilePath = Environment.getExternalStorageDirectory().getParent();
            File rootFile = new File(rootFilePath);
            mp3FilesList = getListFiles(rootFile);



            ArrayList<String> mp3FileNamesList = new ArrayList<String>();
            for(File f: mp3FilesList)
            {
                mp3FileNamesList.add(f.getPath().toString());

            }

            mAdapter = new RecycleViewAdapter(mp3FileNamesList,getApplicationContext());

            playList.setAdapter(mAdapter);
            Log.i("logtest","playlist adapter set");

           *//* playList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {

                    try {
                        Log.i("logtest", "recyclerview was  touched at position:" + position);
                        Log.i("logtest","view name:"+view.getClass().getName());


                        //mAdapter.highlight(view, position);





                            ViewGroup group = (ViewGroup)view;
                            //GlobalVariables.selectedSong = (ViewGroup) view;
                           //Log.i("logtest", "GlobalVariables.selectedSong:" + GlobalVariables.selectedSong.getClass().getName());
                            //GlobalVariables.selectedSong.setBackgroundResource(R.color.colorPrimaryDark);


                            for(int i = 0; i < group.getChildCount(); i++)
                            {
                                View childView = (View)group.getChildAt(i);
                                Log.i("logtest","child view name:"+childView.getClass().getName());

                                if(childView.getClass().getName().endsWith("TextView"))
                                {

                                    if(childView.getTag()!=null)
                                    {

                                        Log.i("logtest", "found textview with tag");
                                        TextView headerTextView = (TextView)findViewById(childView.getId());
                                        Log.i("logtest", "tag:" + childView.getTag());
                                        MyTag tag = (MyTag)childView.getTag();
                                        Log.i("logtest", "tag:" + tag.getMp3FilePath());
                                        Uri songUri = Uri.parse(tag.getMp3FilePath());


                                        MyTag selectedSongTag = new MyTag(true);

                                        childView.setTag(selectedSongTag);


                                        Intent playMusicIntent = new Intent(PlaylistActivity.this, MediaPlayerActivity.class);
                                        playMusicIntent.putExtra("SongPath", songUri.toString());
                                        startActivity(playMusicIntent);





                                    }
                                    else
                                    {
                                        Log.i("logtest","gettag returns null");
                                    }


                                }
                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));

*//*


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK)
        {
            if(data != null && data.getData()!= null)
            {
                Uri audioFileURi = data.getData();
                String MP3Path = audioFileURi.getPath();
                Toast.makeText(getApplicationContext(),MP3Path,Toast.LENGTH_LONG).show();
            }
        }
    }
}
