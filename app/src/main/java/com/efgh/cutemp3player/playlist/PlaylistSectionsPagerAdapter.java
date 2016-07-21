package com.efgh.cutemp3player.playlist;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public class PlaylistSectionsPagerAdapter extends FragmentPagerAdapter
{
    public PlaylistSectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment defaultFragment = new AllSongsPlaylistFragment();
        switch(position)
        {
            case 0 : return new AllSongsPlaylistFragment();

            case 1: return new FoldersPlaylistFragment();



        }

        return defaultFragment;


    }

    @Override
    public int getCount()
    {
        return 2;//TODO add artist,genre, folder, all songs, playlists in sections 'Playlist'
    }


    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence ch = "Songs";
        switch (position)
        {
            case 0 :
                return "Songs";
            case 1:
                return "Folders";

            default:
                return ch;



        }

    }
}
