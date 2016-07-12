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
    private ArrayList<String> mDataset;
    private Context mContext;

    private List<String> pathList;
    private List<Bitmap> imageList;
    private List<String> songTitleList;
    private List<String> albumNameList;

    private Resources currentResources;


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




            currentResources = v.getResources();



        }
    }

    /////////////////////////////////////////////////////////////

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item)
    {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleViewAdapter(ArrayList<String> myDataset, Context mContext)
    {
        mDataset = myDataset;//here we have obtained the mp3FilesList -> array containing the all mp3 file paths
        pathList = new ArrayList<String>();
        imageList = new ArrayList<Bitmap>();
        songTitleList = new ArrayList<String>();
        albumNameList = new ArrayList<String>();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        String jsonString  = prefs.getString("jsonString",null);



        if(jsonString == null)
        {
            Log.i("logtest","first time invoked, writing shared prefs");

            JSONObject rootJSONObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            for(String path: mDataset)
            {
                MetaData metaData = new MetaData(path);


                JSONObject jObj = new JSONObject();

                try
                {
                    jObj.put("path", path);
                    jObj.put("songTitle",metaData.getSongTitle());
                    jObj.put("albumName",metaData.getAlbumName());
                    jsonArray.put(jObj);
                    pathList.add(path);

                }
                catch (JSONException e)
                {
                    Log.i("logtest","exception caught");
                    e.printStackTrace();
                }










            }

            try
            {
                rootJSONObject.put("jsonArray",jsonArray);

                String sharedPrefString = rootJSONObject.toString();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor sPrefEditor = settings.edit();
                sPrefEditor.putString("jsonString", sharedPrefString);
                sPrefEditor.commit();


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                Log.i("logtest","sharedpref wrote successfully");

            }
        }
        else
        {
            Log.i("logtest","NOT first time invoked, reading shared prefs");


            try
            {
               /* JSONObject rootObj = new JSONObject(jsonString);
                JSONArray jArray =  rootObj.getJSONArray("jsonArray");


                for(int i = 0; i < jArray.length(); i++ )
                {

                    JSONObject obj = jArray.getJSONObject(i);
                    String path = obj.getString("path");
                    String songTitle = obj.getString("songTitle");
                    String albumName = obj.getString("albumName");

                    if (songTitle.equals("Unknown artist"))
                    {
                        String s1 = path.substring(0,path.length()-4);
                        int index  =  s1.lastIndexOf('/');
                        songTitle = s1.substring(index+1);
                      //  Log.i("logtest",songTitle);

                    }
*/

                //TODO: metadata creation is consuming a lot of time, have to find a workaround.
                for(int i =0; i < mDataset.size();i++)
                {

                    MetaData mData = new MetaData(mDataset.get(i));
                    songTitleList.add(mData.getSongTitle());
                    albumNameList.add(mData.getAlbumName());;


                    Bitmap albumArt = mData.getAlbumArtBitmap();
                    if(albumArt==null)
                    {
                        Bitmap defaultBitmap = BitmapFactory.decodeResource(currentResources,R.drawable.cover);
                        imageList.add(defaultBitmap);
                    }
                    else
                    {
                        imageList.add(albumArt);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                Log.i("logtest","successfully read existing shared prefs");
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
    public int getItemCount() {
        return mDataset.size();
    }

}
