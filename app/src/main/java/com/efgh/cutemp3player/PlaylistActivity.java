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
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class PlaylistActivity extends AppCompatActivity {

    private int REQ_CODE_PICK_SOUNDFILE = 11;
    private RecyclerView playList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        playList = (RecyclerView)findViewById(R.id.recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        playList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        playList.setLayoutManager(mLayoutManager);

        loadPlaylist();

        // specify an adapter (see also next example)


    }
    public void loadPlaylist()
    {
        try {




            ArrayList<String> mp3FilesList = new ArrayList<String>();

            File file = new File(Environment.getExternalStorageDirectory().getPath());
            String songTitle = "";
            for(File f : file.listFiles())
            {
                if(f.getName().endsWith("mp3"))
                {

                    try
                    {
                        mp3FilesList.add(f.toString());
                    }
                    catch (Exception e)
                    {


                        e.printStackTrace();
                    }

                }
            }



            mAdapter = new RecycleViewAdapter(mp3FilesList);
            playList.setAdapter(mAdapter);

            playList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    int action = event.getAction();
                    if(action == MotionEvent.ACTION_DOWN)
                    {
                        int itemPosition = playList.indexOfChild(v);

                        ViewGroup viewGroup = (ViewGroup)v;
                        View layoutView = viewGroup.getChildAt(0);

                        ViewGroup relLayoutViewGroup = (ViewGroup)layoutView;



                        for(int i=0; i < relLayoutViewGroup.getChildCount(); i++ )
                        {
                            View nextChild = relLayoutViewGroup.getChildAt(i);
                            Log.i("log","child:"+nextChild.getClass().getName());
                            if(nextChild instanceof  TextView)
                            {
                                Log.i("log", " at pos:"+i+"+ obtained textview");
                            }
                        }



                    }
                    return false;
                }
            });



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
