package com.efgh.cutemp3player;

import android.app.ProgressDialog;
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
    ProgressDialogTextChangedListener progressDialogTextChangedListener;

    public RescanMusic(ProgressDialogTextChangedListener listener )
    {
        this.progressDialogTextChangedListener = listener;
    }
    private  List<File> getListFiles(File parentDir)
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



                            this.progressDialogTextChangedListener.updateProgressDialogText(file.getParent());
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
            GlobalFunctions.log("exception caught in rescan music,msg"+e.getClass().getName());
        }

        return inFiles;
    }

    public  List<File> findAllMusicFiles()
    {

        Log.i("logtest","starting music scan");
        String rootLocation = Environment.getExternalStorageDirectory().getParent();
        File rootDirectory = new File(rootLocation);

        List<File> allMusicFiles = getListFiles(rootDirectory);



        Log.i("logtest","completing music scan, files found:"+allMusicFiles.size());
        return allMusicFiles;

    }

}
