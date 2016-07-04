package com.efgh.cutemp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.media.audiofx.Visualizer;

import android.os.Handler;

import java.util.concurrent.TimeUnit;


public class MediaPlayerActivity extends AppCompatActivity {

    private String mPlayerState = GlobalVariables.MediaPlayer.STOPPED;
    private String lastPressed = "";

    private Handler myHandler = new Handler();

    private SeekBar seekBar;

    private MediaPlayer mPlayer;
    private VisualizerView mVisualizerView;
    private Visualizer mVisualizer;

    private double finalTime = 0;
    private double currentTime = 0;

    private int oneTimeOnly = 0;

    private ImageButton playPauseButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);



        playPauseButton = (ImageButton)findViewById(R.id.playButton);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton(v);
            }
        });



        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setClickable(true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null) {
                    currentTime = progress;
                    //currentTime = (int)(finalTime * progress)/100;
                    long currTime = TimeUnit.MILLISECONDS.toSeconds((int) currentTime);
                    long duration = TimeUnit.MILLISECONDS.toSeconds((int) mPlayer.getDuration());

                    if (currTime == duration) {
                        playPauseButton.setImageResource(R.drawable.play);

                        mPlayer.seekTo(0);//TODO: here is the place you will be modifying for repeat-once/always
                        mPlayer.stop();

                    }
                    //
                    Log.i("Log", "progress:" + progress + ",currentTime:" + currentTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null) {
                    mPlayer.seekTo((int) currentTime);
                    mPlayer.start();
                }
            }
        });

        mVisualizerView = (VisualizerView)findViewById(R.id.myvisualizerview);
        Log.d("Log", "oncreate executed, about to start initAudio()");

        initAudio();

    }

    private void initAudio()
    {

        mPlayer = MediaPlayer.create(MediaPlayerActivity.this, R.raw.song);
        mPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mVisualizer.setEnabled(false);
                    }
                });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        setupVisualizerFxAndUI();
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.

        mVisualizer.setEnabled(true);



        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.




    }

    private void setupVisualizerFxAndUI()
    {

        // Create the Visualizer object and attach it to our media player.
        try
        {
            mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(
                    new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer,
                                                          byte[] bytes, int samplingRate) {
                            mVisualizerView.updateVisualizer(bytes);
                        }

                        public void onFftDataCapture(Visualizer visualizer,
                                                     byte[] bytes, int samplingRate) {
                        }
                    }, Visualizer.getMaxCaptureRate() / 2, true, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Log.d("Log","mVisualizer:"+mVisualizer);
        Log.d("Log","mPlayer:"+mPlayer);

      /*

                */
    }



    public void playButton(View v)
    {
        try
        {
            if (mPlayer.isPlaying()==true) // play button changes to pause button
            {

                playPauseButton.setImageResource(R.drawable.pause);



                mPlayer.start();
                mPlayerState = GlobalVariables.MediaPlayer.PLAYING;

                currentTime = mPlayer.getCurrentPosition();
                finalTime = mPlayer.getDuration();


                seekBar.setProgress((int) currentTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                Log.i("Log", "Play button clicked");

                if (oneTimeOnly == 0)
                {
                    seekBar.setMax((int) finalTime);
                    oneTimeOnly = 1;

                }

            }
            else
            {
                if(mPlayer.isPlaying()==false)
                {
                    mPlayer.seekTo((int)currentTime);
                    mPlayer.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                }
                else
                {
                    playPauseButton.setImageResource(R.drawable.play);
                    mPlayer.pause();
                    currentTime = mPlayer.getCurrentPosition();
                    Log.i("Log", "pause button clicked at " + currentTime);
                    mPlayerState = GlobalVariables.MediaPlayer.PAUSED;

                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public void stopButton(View v)
    {

        try {


            if (mPlayer != null) {
                seekBar.setProgress((int)mPlayer.getDuration());
                mPlayer.stop();
                mPlayer = null;
                mPlayerState = GlobalVariables.MediaPlayer.STOPPED;
                Log.i("Log", "Stop button clicked");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private Runnable UpdateSongTime = new Runnable() {



            @Override
            public void run () {
                try {
                    currentTime = mPlayer.getCurrentPosition();
                    seekBar.setProgress((int) currentTime);
                    myHandler.postDelayed(this, 100);
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }

        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
