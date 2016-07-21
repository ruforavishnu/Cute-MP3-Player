package com.efgh.cutemp3player.playlist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efgh.cutemp3player.R;
import com.efgh.cutemp3player.db.DatabaseHandler;
import com.efgh.cutemp3player.global.GlobalFunctions;
import com.efgh.cutemp3player.interfaces.ProgressDialogTextChangedListener;
import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public class AllSongsPlaylistFragment extends Fragment
{
    public static final int ARG_SECTION_NUMBER = 0;
    private RecyclerView playList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ProgressDialog progressDialog;
    private DatabaseHandler dbHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View holder = inflater.inflate(R.layout.blue_fragment_layout, container, false);
        Bundle args = getArguments();

   //     dbHandler = DatabaseHandler.getInstance(getContext());
    //    playList = (RecyclerView) holder.findViewById(R.id.recycler_view);


        return holder;

    }

}
