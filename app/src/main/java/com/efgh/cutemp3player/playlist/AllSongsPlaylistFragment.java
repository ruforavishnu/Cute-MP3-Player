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
import com.efgh.cutemp3player.global.EventNotifier;
import com.efgh.cutemp3player.global.GlobalFunctions;
import com.efgh.cutemp3player.interfaces.IRescanLibraryCompletedListener;

import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.util.List;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public class AllSongsPlaylistFragment extends Fragment implements IRescanLibraryCompletedListener
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
        View holder = inflater.inflate(R.layout.red_fragment_layout, container, false);
        Bundle args = getArguments();

        dbHandler = DatabaseHandler.getInstance(getContext());
        playList = (RecyclerView) holder.findViewById(R.id.allsongsRecyclerView);


        EventNotifier mEventNotifier = EventNotifier.getInstance();
        mEventNotifier.register(this);


        return holder;

    }


    @Override
    public void onRescanComplete(List<MP3MetaData> mList)
    {
        GlobalFunctions.log("rescan completed listener caught from AllSongsPlaylistFragment, mList size:"+mList.size());

       /* mLayoutManager = new CustomLayoutManager(getContext());
        playList.setLayoutManager(mLayoutManager);
        mAdapter = new RecycleViewAdapter(mList, R.layout.recyclerview_layout);

        playList.setAdapter(mAdapter);*/
    }
}
