package com.efgh.cutemp3player.global;

import com.efgh.cutemp3player.interfaces.ISongEventNotifier;
import com.efgh.cutemp3player.interfaces.ISongSelectedListener;
import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 0026,26-Jul-16.
 */
public class SongEventNotifier implements ISongEventNotifier
{
    private List<ISongSelectedListener> observersList;
    private static SongEventNotifier _instance;
    private String songPath;

    public SongEventNotifier()
    {
        observersList = new ArrayList<ISongSelectedListener>();

    }

    public static synchronized SongEventNotifier getInstance()
    {
        if(_instance == null)
        {
            _instance = new SongEventNotifier();
        }
        return _instance;

    }

    public void setSongPath(String  path)
    {
        this.songPath = path;
        notifyObservers(path);
    }
    public String getSongPath()
    {
        return this.songPath;
    }

    @Override
    public void register(ISongSelectedListener listener)
    {
        observersList.add(listener);
    }

    @Override
    public void unregister(ISongSelectedListener listener)
    {
        observersList.remove(listener);
    }

    @Override
    public void notifyObservers(String  currentSongPath)
    {
        for(int i = 0; i < observersList.size(); i++)
        {
            observersList.get(i).onSongSelected(currentSongPath);
        }

    }
}
