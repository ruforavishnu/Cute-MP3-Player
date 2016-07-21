package com.efgh.cutemp3player.playlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.efgh.cutemp3player.R;
import com.efgh.cutemp3player.db.DatabaseHandler;
import com.efgh.cutemp3player.global.GlobalFunctions;
import com.efgh.cutemp3player.interfaces.ProgressDialogTextChangedListener;
import com.efgh.cutemp3player.io.RescanMusic;
import com.efgh.cutemp3player.metadata.MP3MetaData;
import com.efgh.cutemp3player.metadata.MetaDataRetreiver;

import java.io.File;
import java.util.ArrayList;


public class PlaylistActivity extends AppCompatActivity implements ProgressDialogTextChangedListener
{


    private RecyclerView playList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ProgressDialog progressDialog;
    private DatabaseHandler dbHandler;
    private ProgressDialogTextChangedListener mProgressDialogTextChangedListener;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_main_activity);

        this.mProgressDialogTextChangedListener = null;

        ViewPager mViewPager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PlaylistSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);





       /* dbHandler = DatabaseHandler.getInstance(this);

       // playList = (RecyclerView)findViewById(R.id.recycler_view);

        ArrayList<MP3MetaData> list = new ArrayList<MP3MetaData>();

        list = dbHandler.convertDbToArrayList();

        if(list!= null)
        {
            GlobalFunctions.log("list size:"+list.size());
            GlobalFunctions.log("db contains data,reading from db");

            ReadFromDbAsyncTask readDbTask = new ReadFromDbAsyncTask();
            readDbTask.execute();

        }
        else
        {
            GlobalFunctions.log("db does not exist, scanning all music files");

            RescanMusicAsyncTask rescanTask = new RescanMusicAsyncTask();
            rescanTask.execute();

        }*/



    }

    @Override
    public void updateProgressDialogText(String msg)
    {
        GlobalFunctions.log("caught listener");
    }

    public class ReadFromDbAsyncTask extends AsyncTask<Void,Void,Void>
    {

        private ArrayList<MP3MetaData> listFromDb;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            GlobalFunctions.log("ReadFromDbAsyncTask onPreExecute");


        }
        @Override
        protected Void doInBackground(Void... params)
        {
            GlobalFunctions.log("ReadFromDbAsyncTask.. doInBackground");


            listFromDb = new ArrayList<MP3MetaData>();
            listFromDb = dbHandler.convertDbToArrayList();
            GlobalFunctions.log("listFromDb:"+listFromDb);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            GlobalFunctions.log(" ReadFromDbAsyncTask onPostExecute,listFromDb:"+listFromDb);

            //renderRecyclerView(listFromDb);


        }
    }
    /*private void renderRecyclerView(ArrayList<MP3MetaData> mList)
    {
        try
        {
            GlobalFunctions.log("renderRecyclerView ,list recieved by renderRecycler view has size,mList"+mList);
            mLayoutManager = new CustomLayoutManager(this);
            playList.setLayoutManager(mLayoutManager);






            mAdapter = new RecycleViewAdapter(mList);

            playList.setAdapter(mAdapter);
            Log.i("logtest", "playlist adapter set");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            GlobalFunctions.log("exccptino inside renderRecyclerView msg:"+e.getMessage()+"\nstacktrace"+e.getStackTrace().toString());
        }
    }



*/


    public class RescanMusicAsyncTask extends AsyncTask<Void,Void,Void> implements ProgressDialogTextChangedListener
    {

        ArrayList<File> musicFilesList;
        ArrayList<MP3MetaData> listOfAllMetaData;


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            GlobalFunctions.log("RescanMusicAsyncTask onPreExecute");

            progressDialog = new ProgressDialog(PlaylistActivity.this);
            progressDialog.setTitle("Rescan library");

            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Run in background", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.i("logtest", "run in backgrnd clicked");
                    progressDialog.hide();//TODO might need to be changed
                }
            });
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.i("logtest", "cancel clicked");

                }
            });




            progressDialog.setMessage("Starting scan...");
            progressDialog.show();

            musicFilesList = new ArrayList<File>();
            listOfAllMetaData = new ArrayList<MP3MetaData>();


        }



        @Override
        protected Void doInBackground(Void... params)
        {



            try
            {
                GlobalFunctions.log("RescanMusicAsyncTask doInBackground");


                RescanMusic musicScanner = new RescanMusic(this);
                musicFilesList = musicScanner.findAllMusicFiles();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressDialog.setMessage("Retrieving metadata...");
                    }
                });

                GlobalFunctions.log("music files located, starting metadata retrievel...,total files:"+musicFilesList.size());
                MetaDataRetreiver metaDataRetreiver = new MetaDataRetreiver();
                listOfAllMetaData = metaDataRetreiver.findMP3MetaDataList(musicFilesList,getResources());

                GlobalFunctions.log("meta data retrieved, mp3MetaDataList:"+listOfAllMetaData);


                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressDialog.setMessage("Saving to database...");
                    }
                });


                GlobalFunctions.log("RescanMusicAsyncTask  doInBackground, about to convert arraylist to db, listOfAllMetaData size:" + listOfAllMetaData.size());


                dbHandler.convertArrayListToDB(listOfAllMetaData,PlaylistActivity.this);





            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.i("logtest","exc caught at :"+e.getClass().getName());
                int i = 0;

            }


            return null;
        }





        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            GlobalFunctions.log("RescanMusicAsyncTask onPostExecute");

            //   progressDialog.dismiss();

            Log.i("logtest", "onPostExecute");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    progressDialog.dismiss();



                    GlobalFunctions.log("b4 passing to render recycler view, mList:"+listOfAllMetaData);
                    //renderRecyclerView(listOfAllMetaData);
                }
            });






        }

        @Override
        public void updateProgressDialogText(final String msg)
        {
            //GlobalFunctions.log("listener caught");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    progressDialog.setMessage(msg);
                }
            });

        }
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

    }
}
