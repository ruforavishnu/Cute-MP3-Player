package com.efgh.cutemp3player.playlist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efgh.cutemp3player.R;
import com.efgh.cutemp3player.db.DatabaseHandler;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public class FoldersPlaylistFragment extends Fragment
{
    public static final int ARG_SECTION_NUMBER = 1;
    private RecyclerView playList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private ProgressDialog progressDialog;
    private DatabaseHandler dbHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View holder = inflater.inflate(R.layout.red_fragment_layout, container, false);
        Bundle args = getArguments();

     //   dbHandler = DatabaseHandler.getInstance(getContext());
       // playList = (RecyclerView) holder.findViewById(R.id.recycler_view);


        return holder;

    }

}
