package com.keeneye.musicplayer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bazilm on 10-06-2015.
 */

public class ListAdapter extends ArrayAdapter<GetArtist.Artist> {

    private Context context;
    private ArrayList<GetArtist.Artist> artists= new ArrayList<GetArtist.Artist>();

    public ListAdapter(Context context, ArrayList<GetArtist.Artist> values) {
        super(context, -1,values);
        this.context=context;
        this.artists=values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item,parent,false);
        TextView textView = (TextView)rowView.findViewById(R.id.list_name_item);
        ImageView imageView =(ImageView) rowView.findViewById(R.id.list_image_item);
        textView.setText(artists.get(position).getName());
        if(artists.get(position).getImgUrl()!=null)
        imageView.setImageURI(Uri.parse(artists.get(position).getImgUrl()));
        return rowView;
    }
}
