package com.efgh.cutemp3player;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 15-Jul-16.
 */
public class RescanMusic
{
    public RescanMusic()
    {

    }
    private static List<File> getListFiles(File parentDir)
    {

        boolean exceptionCaught = false;
        ArrayList<File> inFiles = null;
        try
        {
            inFiles = new ArrayList<File>();
            File[] files = parentDir.listFiles();

            if(files != null)
            {

                for (File file : files)
                {
                    if(file != null )
                    {
                        if (file.isDirectory())
                        {
                            inFiles.addAll(getListFiles(file));
                        }
                        else
                        {
                            if(file.getName().endsWith(".mp3"))
                            {

                                inFiles.add(file);
                            }
                        }
                    }

                }
            }
        }
        catch (Exception e)
        {
            exceptionCaught = true;
            e.printStackTrace();
        }

        return inFiles;
    }
    public static List<File> findAllMusicFiles()
    {

        String rootLocation = Environment.getExternalStorageDirectory().getParent();
        File rootDirectory = new File(rootLocation);
        List<File> allMusicFiles = getListFiles(rootDirectory);


        return allMusicFiles;

    }

}
