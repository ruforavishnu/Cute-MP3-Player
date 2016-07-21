package com.efgh.cutemp3player.interfaces;

import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.util.List;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public interface IEventNotifier
{
    void register(IRescanLibraryCompletedListener listener);
    void unregister(IRescanLibraryCompletedListener listener);
    void notifyObservers(List<MP3MetaData> mp3MetaDataList);

}
