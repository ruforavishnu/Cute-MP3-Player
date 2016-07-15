package com.efgh.cutemp3player;

/**
 * Created by Vishnu on 05-Jul-16.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>
{
    private List<MP3MetaData> mDataset;
    private Context mContext;

    private List<String> pathList;
    private List<Bitmap> imageList;
    private List<String> songTitleList;
    private List<String> albumNameList;

    private Resources currentResources;



    public void highlight(View view, int position)
    {

    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView albumArtImageView;

        public ViewHolder(View v)
        {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.songtitle);
            txtFooter = (TextView) v.findViewById(R.id.albumname);
            albumArtImageView = (ImageView)v.findViewById(R.id.thumbnail);

            txtHeader.setText("Unknown artist");
            txtFooter.setText("Audio");
            albumArtImageView.setImageResource(R.drawable.cover);

            v.setClickable(true);




            currentResources = v.getResources();



        }
    }

    /////////////////////////////////////////////////////////////

    public void add(int position, MP3MetaData item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(MP3MetaData item)
    {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleViewAdapter(List<MP3MetaData> myDataset, Context mContext)
    {
        mDataset = myDataset;//here we have obtained the mp3FilesList -> array containing the all mp3 file paths
        pathList = new ArrayList<String>();
     //   imageList = new ArrayList<byte[]>();
        songTitleList = new ArrayList<String>();
        albumNameList = new ArrayList<String>();

        for(int i =0; i < mDataset.size();i++)
        {

            MP3MetaData mp3MetaData = mDataset.get(i);

            songTitleList.add(mp3MetaData.getSongTitle());
            albumNameList.add(mp3MetaData.getAlbumTitle());
            Log.i("logtest", "inside metadata class, songtitle:" + mp3MetaData.getSongTitle());
            Log.i("logtest","inside metadata class, albumNameList:"+mp3MetaData.getAlbumTitle());




            byte[] albumArt = mp3MetaData.getAlbumArt();

            if(albumArt==null)
            {
                Bitmap defaultBitmap = BitmapFactory.decodeResource(currentResources,R.drawable.cover);
                imageList.add(defaultBitmap);
            }
            else
            {
                Bitmap defaultBitmap = BitmapFactory.decodeByteArray(albumArt,0,albumArt.length);
                imageList.add(defaultBitmap);
            }



        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element



        try
        {

            Bitmap img = imageList.get(position);
            if(img == null)
            {
                holder.albumArtImageView.setImageResource(R.drawable.cover);
            }
            else
            {
                holder.albumArtImageView.setImageBitmap(imageList.get(position));
            }
            holder.txtHeader.setText(songTitleList.get(position));
            holder.txtFooter.setSelected(true);

            holder.txtFooter.setText(albumNameList.get(position));

            /*MyTag pathTag = new MyTag(mDataset.get(position));

            holder.txtHeader.setTag(pathTag);
*/




        }
        catch (Exception e)
        {

            e.getMessage();
            e.printStackTrace();
        }






    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
