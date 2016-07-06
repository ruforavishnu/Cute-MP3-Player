package com.efgh.cutemp3player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.media.audiofx.Visualizer;

import android.os.Handler;
import android.widget.TextView;

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
    private TextView durationTextView;
    private ImageView albumArtImageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);


        durationTextView = (TextView)findViewById(R.id.durationText);
        playPauseButton = (ImageButton)findViewById(R.id.playButton);

        albumArtImageView = (ImageView)findViewById(R.id.albumArtImageView);


            MediaMetadataRetriever songDetailsRetriever = new MediaMetadataRetriever();

            songDetailsRetriever.setDataSource("/sdcard/audio.mp3");
            byte[] art = songDetailsRetriever.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            albumArtImageView.setImageBitmap(songImage);


        catch (Exception e)
        {
            e.printStackTrace();
        }
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

                    currentTime = progress;

                    long currTime = TimeUnit.MILLISECONDS.toSeconds((int) currentTime);
                    long duration = TimeUnit.MILLISECONDS.toSeconds( mPlayer.getDuration());

                    if (currTime == duration)
                    {
                        playPauseButton.setImageResource(R.drawable.play);

                        mPlayer.seekTo(0);//TODO: here is the place you will be modifying for repeat-once/always
                        mPlayer.pause();

                    }

                 //   Log.i("Log", "progress:" + progress + ",currentTime:" + currentTime);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


                if (mPlayer.isPlaying())
                {
                  //  mPlayer.pause();
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {




                    mPlayer.seekTo((int) currentTime);
               //     mPlayer.start();
                    Log.d("Log", "currentTime:"+currentTime);
            }

        });

        mVisualizerView = (VisualizerView)findViewById(R.id.myvisualizerview);


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
        finalTime = mPlayer.getDuration();

        seekBar.setMax((int) finalTime);



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

    }



    public void playButton(View v)
    {
        Log.d("Log","mPlayer.isPlaying():"+mPlayer.isPlaying());
        try
        {

            if (mPlayer.isPlaying()==true) // play/pause  button clicked when song is playing-->show play button and pause music
            {
                mPlayer.pause();// pausing the music
                playPauseButton.setImageResource(R.drawable.play);// since song is paused show play button image
                currentTime = mPlayer.getCurrentPosition();// remember the position where the music was paused


            }
            else if(mPlayer.isPlaying()==false)// play/pause button clicked when song is not playing--> show pause button and play music
            {

                //Log.d("Log","")


                mPlayer.start();// start playing music
                playPauseButton.setImageResource(R.drawable.pause);//since song is playing show pause button image
                myHandler.postDelayed(UpdateSongTime, 100);// use a Runnable Thread instance to run code every 100ms


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
            playPauseButton.setImageResource(R.drawable.play);// since song is stopped show play button image

            seekBar.setProgress(0);
            mPlayer.seekTo(0);

            mPlayer.pause();// stop playing the music




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
                   // if(mPlayer.isPlaying()==true)
                    {
                        currentTime = mPlayer.getCurrentPosition();
                        seekBar.setProgress((int) currentTime);

                        String currentTimeString = calcTimeInMinsAndSecs(currentTime);
                        String totalTimeString = calcTimeInMinsAndSecs(finalTime);




                        durationTextView.setText(currentTimeString+"/"+totalTimeString);
                        Log.i("Log", "runnable thread running");

                        myHandler.postDelayed(this, 100);
                    }
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }

        }

    };

    private String calcTimeInMinsAndSecs(double time)
    {
        String result = "";
        long currMinutes = TimeUnit.MILLISECONDS.toMinutes((int) time);
        long currSeconds = TimeUnit.MILLISECONDS.toSeconds((int)time) - (currMinutes * 60);
        String currSecondsWithPrecedingZero = (currSeconds < 10) ? "0"+ String.valueOf(currSeconds) : String.valueOf(currSeconds);
        result = String.valueOf(currMinutes)+":"+ currSecondsWithPrecedingZero;

        return result;
    }

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
