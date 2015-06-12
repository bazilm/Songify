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
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by bazilm on 10-06-2015.
 */

public class ListAdapter<T> extends ArrayAdapter<T> {


    private Context context;
    private ArrayList<T> results = new ArrayList<T>();
    public ImageView imageView;
    private Class<T> type;


    public ListAdapter(Context context, ArrayList<T> values,Class<T> type) {
        super(context, -1,values);
        this.context=context;
        this.results =values;
        this.type = type;


    }


    @Override
    public T getItem(int position) {
        return type.cast(results.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item,parent,false);
        TextView textView = (TextView)rowView.findViewById(R.id.list_name_item);
        imageView =(ImageView) rowView.findViewById(R.id.list_image_item);

        if(type == Artist.class) {
            textView.setText(((Artist)(results.get(position))).name);

            int size = ((Artist)(results.get(position))).images.size();
            if (size > 0) {
                String imgUrl = ((Artist)(results.get(position))).images.get(size - 1).url;
                Glide.with(getContext()).load(imgUrl).into(imageView);
            }
        }

        else if(type== Track.class){

            textView.setText(((Track) (results.get(position))).name);

            int size = ((Track)(results.get(position))).album.images.size();
            if (size > 0) {
                String imgUrl = ((Track)(results.get(position))).album.images.get(size - 1).url;
                Glide.with(getContext()).load(imgUrl).into(imageView);
            }

        }


        return rowView;
    }

    public ArrayList<T> getValues()
    {
        return results;


    }
}
