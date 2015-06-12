package com.keeneye.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by bazilm on 10-06-2015.
 */

public class ListAdapter extends ArrayAdapter<Artist> {


    private Context context;
    private ArrayList<Artist> artists= new ArrayList<Artist>();
    public ImageView imageView;



    public ListAdapter(Context context, ArrayList<Artist> values) {
        super(context, -1,values);
        this.context=context;
        this.artists=values;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item,parent,false);
        TextView textView = (TextView)rowView.findViewById(R.id.list_name_item);
        imageView =(ImageView) rowView.findViewById(R.id.list_image_item);
        SearchActivity searchActivity = ((SearchActivity)context);

        textView.setText(artists.get(position).name);

        int size = artists.get(position).images.size();
        if(size>0) {
            String imgUrl = artists.get(position).images.get(size - 1).url;
            Glide.with(getContext()).load(imgUrl).into(imageView);
        }



        return rowView;
    }
}
