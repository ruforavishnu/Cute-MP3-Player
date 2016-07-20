package com.efgh.cutemp3player;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Vishnu on 19-Jul-16.
 */
public class RescanMusicDialogFragment extends DialogFragment
{
    TextView progressText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder rescanmusicDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View holder = inflater.inflate(R.layout.scan_music_dialog,null);

        rescanmusicDialog.setView(holder);

        rescanmusicDialog.setMessage("Rescan Library")
                .setPositiveButton("Run in background", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //run in background
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //cancel scanning
                    }
                });

        progressText = (TextView)holder.findViewById(R.id.progressText);
        progressText.setText("changed progress value");





        return rescanmusicDialog.create();
    }
    public void setProgressText(String msg)
    {
        if(progressText!=null)
        {
            progressText.setText(msg);

        }

    }


}
