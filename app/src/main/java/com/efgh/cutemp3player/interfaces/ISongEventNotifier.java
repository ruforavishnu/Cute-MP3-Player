package com.efgh.cutemp3player.interfaces;

import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.util.List;

/**
 * Created by Vishnu on 0026,26-Jul-16.
 */
public interface ISongEventNotifier
{
    void register(ISongSelectedListener listener);
    void unregister(ISongSelectedListener listener);
    void notifyObservers(String  currentSongPath);
}
