package com.efgh.cutemp3player;

import android.media.MediaPlayer;

/**
 * Created by Vishnu on 04-Jul-16.
 */
public class GlobalVariables
{
    public static  final int MP3_PATH_KEY = 54;
    public static int timesInvoked = 0;
    public static android.media.MediaPlayer mPlayer;
    public static class MediaPlayer
    {
        public static String PLAYING = "playing";
        public static String PAUSED = "paused";
        public static String STOPPED = "stopped";
        public static String INACTIVE = "inactive";

        public static class LastPressedButton
        {
            public static int id = -54321;

        }

    }
}
