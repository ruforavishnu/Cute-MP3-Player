package com.efgh.cutemp3player;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private int REQ_CODE_PICK_SOUNDFILE = 11;
    private RecyclerView playList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        playList = (RecyclerView)findViewById(R.id.recycler_view);
        mLayoutManager = new CustomLayoutManager(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        playList.setHasFixedSize(true);
        playList.setItemViewCacheSize(6);
        playList.setDrawingCacheEnabled(true);
        playList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);


        // use a linear layout manager


        playList.setLayoutManager(mLayoutManager);

        loadPlaylist();

        // specify an adapter (see also next example)


    }
    private List<File> getListFiles(File parentDir)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".mp3"))
                {
                  //  Log.i("logtest","filepath:"+file.getPath());

                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
    public void loadPlaylist()
    {
        try {




            List<File> mp3FilesList = new ArrayList<File>();
            String filePath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filePath);
            mp3FilesList = getListFiles(file);

            ArrayList<String> mp3FileNamesList = new ArrayList<String>();
            for(File f: mp3FilesList)
            {
                mp3FileNamesList.add(f.getPath().toString());
               // Log.i("logtest", "mp3 file found:" + f.getPath().toString());

            }






            mAdapter = new RecycleViewAdapter(mp3FileNamesList,getApplicationContext());

            playList.setAdapter(mAdapter);

            playList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {

                    try {
                        Log.i("logtest", "recyclerview was  touched at position:" + position);
                        Log.i("logtest","view name:"+view.getClass().getName());

                        ViewGroup group = (ViewGroup) view;
                        if(GlobalVariables.selectedSong != null)
                        {
                            GlobalVariables.selectedSong.setBackgroundResource(R.color.normal);
                        }

                        GlobalVariables.selectedSong = (ViewGroup) group;
                        group.setBackgroundResource(R.color.colorPrimaryDark);


                        for(int i = 0; i < group.getChildCount(); i++)
                        {
                            View childView = (View)group.getChildAt(i);
                            Log.i("logtest","view name:"+childView.getClass().getName());

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




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

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
