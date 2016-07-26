package com.efgh.cutemp3player.playlist;

/**
 * Created by Vishnu on 05-Jul-16.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.efgh.cutemp3player.R;
import com.efgh.cutemp3player.global.GlobalFunctions;
import com.efgh.cutemp3player.metadata.MP3MetaData;
import com.efgh.cutemp3player.metadata.MyTag;


public  class AllSongsRecyclerViewAdapter extends RecyclerView.Adapter<AllSongsRecyclerViewAdapter.ViewHolder>
{
    private List<MP3MetaData> mDataset;
    private Context mContext;

    private List<String> pathList;
    private List<Bitmap> imageList;
    private List<String> songTitleList;
    private List<String> albumNameList;


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
            txtHeader.setSelected(true);
            txtFooter.setText("Audio");
            albumArtImageView.setImageResource(R.drawable.newplaybutton);

            v.setClickable(true);


            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    GlobalFunctions.log("clicked on view, song name:" + txtHeader.getText().toString());
                    MyTag myTagObj = (MyTag)txtHeader.getTag();
                    GlobalFunctions.log("clicked songs path:"+myTagObj.getMp3FilePath());
                }
            });


        }
    }

    /////////////////////////////////////////////////////////////

    public  void add(int position, MP3MetaData item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public  void remove(MP3MetaData item)
    {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AllSongsRecyclerViewAdapter(List<MP3MetaData> myDataset)
    {
        mDataset = myDataset;//here we have obtained the mp3FilesList -> array containing the all mp3 file paths
        pathList = new ArrayList<String>();
        imageList = new ArrayList<Bitmap>();
        songTitleList = new ArrayList<String>();
        albumNameList = new ArrayList<String>();



        for(int i =0; i < mDataset.size();i++)
        {


            MP3MetaData mp3MetaData = mDataset.get(i);

            songTitleList.add(mp3MetaData.getSongTitle());
            albumNameList.add(mp3MetaData.getAlbumTitle());
            Bitmap defaultBitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.newplaybutton);
            imageList.add(defaultBitmap);
            pathList.add((mp3MetaData.getPath()));



        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public  AllSongsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public  void onBindViewHolder(final ViewHolder holder, int position)
    {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element



        try
        {

            Bitmap img = imageList.get(position);
            holder.albumArtImageView.setImageResource(R.drawable.newplaybutton);

            holder.txtHeader.setText(songTitleList.get(position));
            holder.txtFooter.setSelected(true);

            holder.txtFooter.setText(albumNameList.get(position));

            MyTag pathTag = new MyTag(pathList.get(position));

            holder.txtHeader.setTag(pathTag);





        }
        catch (Exception e)
        {

            e.getMessage();
            e.printStackTrace();
        }






    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public  int getItemCount() {
        return mDataset.size();
    }

}
