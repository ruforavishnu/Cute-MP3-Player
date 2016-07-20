package com.efgh.cutemp3player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PlaylistActivity extends AppCompatActivity {

    private int REQ_CODE_PICK_SOUNDFILE = 11;
    private RecyclerView playList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

    RescanMusicDialogFragment rescanDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);







        rescanMusic();








    }
    public void rescanMusic()
    {
        Log.i("logtest","starting to scan for music");
        RescanMusicAsyncTask rescanTask = new RescanMusicAsyncTask();
        rescanTask.execute();
    }
    public class RescanMusicAsyncTask extends AsyncTask<Void,Void,Void>
    {

        private AlertDialog.Builder rescanDialog;
        private AlertDialog mAlertDialog;
        private ProgressDialog progressDialog;






        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*rescanDialog = new AlertDialog.Builder(PlaylistActivity.this);
            rescanDialog.setTitle("Rescan library");
            rescanDialog.setMessage("path");
            mAlertDialog = rescanDialog.create();
            mAlertDialog.show();*/

            progressDialog = new ProgressDialog(PlaylistActivity.this);
            progressDialog.setTitle("Rescan library");

            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Run in background", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.i("logtest", "run in backgrnd clicked");
                }
            });
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Log.i("logtest","cancel clicked");

                }
            });

            progressDialog.setMessage("Starting scan...");
            progressDialog.show();

            musicFilesList = new ArrayList<File>();








        }


        @Override
        protected Void doInBackground(Void... params)
        {

            try
            {


                RescanMusic musicScanner = new RescanMusic(progressDialog);
                musicFilesList = musicScanner.findAllMusicFiles();




            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.i("logtest","exc message:"+e.getMessage());
                int i = 0;

            }




            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
         //   progressDialog.dismiss();
            Log.i("logtest", "onPostExecute");
            progressDialog.dismiss();

           /* progressWindow.setMessage("Completed");
            alertDialog.dismiss();


            renderRecyclerView();*/

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
