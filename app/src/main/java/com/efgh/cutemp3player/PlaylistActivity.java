package com.efgh.cutemp3player;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private int REQ_CODE_PICK_SOUNDFILE = 11;
    private RecyclerView playList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        playList = (RecyclerView)findViewById(R.id.recycler_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadPlaylist();

        // specify an adapter (see also next example)


    }
    public void loadPlaylist()
    {
        try {


            Log.d("Log","chooseSong button clicked");




            ArrayList<String> mp3Files = new ArrayList<String>();

            File file = new File("/sdcard/");
            String albumName = "";
            for(File f : file.listFiles())
            {
                if(f.getName().endsWith("mp3"))
                {

                    try {
                        MediaMetadataRetriever songDetailsRetriever = new MediaMetadataRetriever();

                        songDetailsRetriever.setDataSource(f.toString());

                        try
                        {

                            byte[] art = songDetailsRetriever.getEmbeddedPicture();
                            Bitmap songImage = BitmapFactory.decodeByteArray(art,0,art.length);

                        }
                        catch (Exception e)
                        {

                        }

                        albumName = songDetailsRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                        mp3Files.add(f.toString());
                    }
                    catch (Exception e)
                    {
                        Log.d("Log","reading album tag caught exception");

                        e.printStackTrace();
                    }
                    finally {

                    }
                }
            }



            mAdapter = new RecycleViewAdapter(mp3Files);

            mRecyclerView.setAdapter(mAdapter);





        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i("Log", "inside onActivityResult function ");
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
