package com.efgh.cutemp3player.global;

import com.efgh.cutemp3player.interfaces.IEventNotifier;
import com.efgh.cutemp3player.interfaces.IRescanLibraryCompletedListener;
import com.efgh.cutemp3player.metadata.MP3MetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu on 21-Jul-16.
 */
public class EventNotifier implements IEventNotifier
{



    private List<MP3MetaData> mp3MetaDataList;

    private List<IRescanLibraryCompletedListener> observersList;



    private static EventNotifier _instance;




    public EventNotifier()
    {
        observersList = new ArrayList<IRescanLibraryCompletedListener>();

    }

    public static synchronized EventNotifier getInstance()
    {
        if(_instance == null)
        {
            _instance = new EventNotifier();
        }
        return _instance;

    }
    public List<MP3MetaData> getMp3MetaDataList()
    {
        return mp3MetaDataList;
    }

    public void setMp3MetaDataList(List<MP3MetaData> mp3MetaDataList)
    {
        this.mp3MetaDataList = mp3MetaDataList;
        notifyObservers(mp3MetaDataList);
    }


    @Override
    public void register(IRescanLibraryCompletedListener listener)
    {
        observersList.add(listener);
    }

    @Override
    public void unregister(IRescanLibraryCompletedListener listener)
    {
        observersList.remove(listener);
    }

    @Override
    public void notifyObservers(List<MP3MetaData> mp3MetaDataList)
    {
        GlobalFunctions.log("inside notifyObservers method, observerslist :"+observersList.size());
        for(int i = 0; i < observersList.size(); i++)
        {
            observersList.get(i).onRescanComplete(mp3MetaDataList);
        }
    }

}
